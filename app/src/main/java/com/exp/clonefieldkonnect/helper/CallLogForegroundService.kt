package com.exp.clonefieldkonnect.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.database.Cursor
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.CallLog
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.LeadActivity
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CallLogForegroundService : Service() {

    private var handler: Handler? = null
    private var lastLoggedCallTime: Long = -1L

    companion object {
        private const val CHANNEL_ID = "call_log_service_channel"
        private const val NOTIFICATION_ID = 1002
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        startForegroundServiceWithNotification()
        startRepeatingCallLogCheck()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingCallLogCheck()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundServiceWithNotification() {
        val channelId = "call_log_channel"
        val channelName = "Call Log Tracking"

        // Create notification channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tracking Call Logs")
            .setContentText("Monitoring call status in background…")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // ✅ On Android 10+ with targetSdk 34/35, you must specify service type
            startForeground(
                1,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
            )
        } else {
            startForeground(1, notification)
        }
    }


    private fun startRepeatingCallLogCheck() {
        handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                fetchLatestCallLog()
                handler?.postDelayed(this, 30_000L) // check every 30 seconds
            }
        }
        handler?.post(runnable)
    }

    private fun stopRepeatingCallLogCheck() {
        handler?.removeCallbacksAndMessages(null)
    }

    @SuppressLint("Range", "MissingPermission")
    private fun fetchLatestCallLog() {
        if (!hasCallLogPermission()) {
            Log.e("CallLogService", "❌ Call log permission not granted.")
            stopSelf() // stop if permission missing
            return
        }

        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val number = it.getString(it.getColumnIndex(CallLog.Calls.NUMBER))
                val duration = it.getInt(it.getColumnIndex(CallLog.Calls.DURATION))
                val date = it.getLong(it.getColumnIndex(CallLog.Calls.DATE))

                val appNumber = StaticSharedpreference.getInfo(Constant.CALLING_NUMBER, this)

                // Only process if this call matches the number initiated from app
                if (appNumber.isNullOrEmpty() || !number.contains(appNumber)) {
                    Log.d("CallLogService", "Ignored call from: $number")
                    stopSelf()
                    return
                }

                if (date == lastLoggedCallTime) {
                    stopSelf()
                    return
                }
                lastLoggedCallTime = date

                val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(Date(date))
                val callStatus = if (duration > 0) 1 else 0

                Log.d("CallLogService", "📴 Call Ended: $number, Duration=$duration, Time=$dateTime")

                sendCallUpdateToApi(number, duration, callStatus, dateTime)

                // Clear the saved number after processing
                StaticSharedpreference.saveInfo(Constant.CALLING_NUMBER, "", this)

                stopSelf() // ✅ Stop service after handling
            }
        }

        // ✅ Stop service after one fetch
        stopSelf()
    }


    private fun hasCallLogPermission(): Boolean {
        val readLog = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
        val phoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        return readLog == PackageManager.PERMISSION_GRANTED &&
                phoneState == PackageManager.PERMISSION_GRANTED
    }


    private fun sendCallUpdateToApi(number: String, duration: Int, status: Int, dateTime: String) {
        var lead_id = StaticSharedpreference.getInfo(Constant.Call_lead_id, this).toString()

        val queryParams = HashMap<String, String>()
        queryParams["lead_id"] = lead_id
        queryParams["started_at"] = dateTime
        queryParams["duration"] = duration.toString()
        queryParams["status"] = status.toString()
        queryParams["number"] = number

        println("queryParamscallllll"+queryParams)

        ApiClient.submitleadcalllog(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                override fun onAPIResult(
                    response: retrofit2.Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            Log.d("CallLogService", "✅ Call log submitted successfully")
                        } else {
                            Log.e("CallLogService", "❌ Failed: ${response.errorBody()?.string()}")
                        }
                    }
                }
            }
        )
    }

}

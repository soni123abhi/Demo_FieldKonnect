package com.exp.clonefieldkonnect.helper

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import android.util.Log
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CallTracker {

    private var callStatusListener: CallStatusListener? = null
    private var lastLoggedCallTime: Long = 0
    private var callLogObserver: CallLogObserver? = null

    fun startTracking(context: Context, listener: CallStatusListener) {
        callStatusListener = listener

        // Register observer for call log changes
        if (callLogObserver == null) {
            callLogObserver = CallLogObserver(context, Handler(Looper.getMainLooper()))
            context.contentResolver.registerContentObserver(
                CallLog.Calls.CONTENT_URI,
                true,
                callLogObserver!!
            )
        }
    }

    private fun fetchLastOutgoingCall(context: Context) {
        try {
            val cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                "${CallLog.Calls.TYPE} = ?",
                arrayOf(CallLog.Calls.OUTGOING_TYPE.toString()),
                CallLog.Calls.DATE + " DESC"
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val number = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val duration = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val date = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))

                    // Deduplicate using timestamp
                    if (date == lastLoggedCallTime) return
                    lastLoggedCallTime = date

                    val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(date))
                    val callStatus = if (duration > 0) 1 else 0

                    println("📴 Outgoing call ended: $number")
                    println("⏱ Duration: $duration seconds")
                    println("🗓 DateTime: $dateTime")

                    val intent = Intent(context!!, CallLogForegroundService::class.java)
                    ContextCompat.startForegroundService(context!!, intent)

//                    callStatusListener?.onCallEnded(number, duration, callStatus, dateTime)
                }
            }
        } catch (e: SecurityException) {
            Log.e("CallTracker", "Missing READ_CALL_LOG permission", e)
        }
    }

    private class CallLogObserver(context: Context, handler: Handler) : ContentObserver(handler) {
        private val appContext = context.applicationContext
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            fetchLastOutgoingCall(appContext)
        }
    }

    fun stopTracking(context: Context) {
        callLogObserver?.let {
            context.contentResolver.unregisterContentObserver(it)
            callLogObserver = null
        }
        callStatusListener = null
    }
}

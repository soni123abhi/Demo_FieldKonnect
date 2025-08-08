package com.exp.clonefieldkonnect.worker

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.LocationRequestModel
import com.google.android.gms.location.*
import com.google.gson.JsonObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LocationForegroundService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var handler: Handler? = null
    private lateinit var locationRequest: LocationRequest
    private var locationCallback: LocationCallback? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundServiceWithNotification()
        initLocationUpdates()
        startRepeatingLocationTask()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingLocationTask()
        fusedLocationClient.removeLocationUpdates(locationCallback!!)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundServiceWithNotification() {
        val channelId = "location_service_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Tracking")
            .setContentText("Fetching location every 1 minute.")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    @SuppressLint("MissingPermission")
    private fun initLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 900000L)
            .setMinUpdateIntervalMillis(900000L) // 1 min
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    println("Abhiii=="+location.latitude.toString()+"<<"+location.longitude.toString())
                    sendLocationUpdate(location.latitude.toString(), location.longitude.toString())
                }
            }
        }
    }

    private fun startRepeatingLocationTask() {
        handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (hasLocationPermission()) {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback!!, Looper.getMainLooper())
                } else {
                    Log.e("LocationService", "Location permission not granted.")
                }
                handler?.postDelayed(this, 900000L) // every 1 minute
            }
        }
        handler?.post(runnable)
    }

    private fun stopRepeatingLocationTask() {
        handler?.removeCallbacksAndMessages(null)
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val fgService = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION)
        } else {
            PackageManager.PERMISSION_GRANTED
        }
        return fine == PackageManager.PERMISSION_GRANTED &&
                coarse == PackageManager.PERMISSION_GRANTED &&
                fgService == PackageManager.PERMISSION_GRANTED
    }

    private fun sendLocationUpdate(lat: String, lon: String) {
        val locationRequestModel = LocationRequestModel()

        val locations = locationRequestModel.Locations().apply {
            latitude = lat
            longitude = lon
            time = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())
        }

        locationRequestModel.locations = arrayListOf(locations)

        ApiClient.updateLiveLocation(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, applicationContext).toString(),
            locationRequestModel,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    if (response != null && response.isSuccessful) {
                        println("Abhiii==M="+"Location update sent successfully")
                        Log.d("LocationService", "Location update sent successfully")
                    } else {
                        Log.e("LocationService", "Failed to send location: $errorMessage")
                    }
                }
            }
        )
    }
}

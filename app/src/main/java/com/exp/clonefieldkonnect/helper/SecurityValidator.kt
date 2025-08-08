package com.exp.clonefieldkonnect.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.exp.import.Utilities
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.net.NetworkInterface

object SecurityValidator {

    private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        ) != 0
    }

    fun isVpnActive(): Boolean {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                if (!networkInterface.isUp || networkInterface.interfaceAddresses.isEmpty()) continue

                val name = networkInterface.name
                if (name.equals("tun0", ignoreCase = true) || name.equals("ppp0", ignoreCase = true)) {
                    return true // VPN detected
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Call this from activity when ready to request permissions
     */
    fun requestLocationPermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    /**
     * Checks if fake location is detected. Calls callback with result.
     */
    @SuppressLint("MissingPermission")
    fun isFakeLocationDetected(activity: Activity, callback: (Boolean) -> Unit) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("SecurityValidator", "Location permission not granted")
            callback(false)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null && location.isFromMockProvider) {
                Log.d("SecurityValidator", "Fake location detected.")
                callback(true)
            } else {
                callback(false)
            }
        }.addOnFailureListener {
            Log.e("SecurityValidator", "Location fetch failed: ${it.message}")
            callback(false)
        }
    }


    fun checkEnvironmentAndBlock(activity: Activity) {
        if (activity.isFinishing) return

        requestLocationPermission(activity)

        isFakeLocationDetected(activity) { isFake ->
            if (isFake) {
                Utilities.showExitDialog(
                    context = activity,
                    message = "Fake location detected. Please disable mock location apps."
                )
            } else {
                Log.d("SecurityValidator", "Location is clean (not fake).")
            }
        }

        if (isDeveloperOptionsEnabled(activity)) {
            Utilities.showExitDialog(
                context = activity,
                message = "Developer options are enabled on your device. Please disable them to continue using this app."
            )
            return
        }

        if (isVpnActive()) {
            Utilities.showExitDialog(
                context = activity,
                message = "VPN connection detected. Please disable VPN to use the app."
            )
            return
        }

    }
}

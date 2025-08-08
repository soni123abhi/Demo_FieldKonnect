package com.exp.clonefieldkonnect.worker

import android.content.Context
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.LocationRequestModel
import com.google.gson.JsonObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WorkerLocation(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
        var gps_enabled = false
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.v("akram", "call worker in gajra")
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        if (gps_enabled) {
           // gettingLocation()
        }

        return Result.success()
    }

/*
    private fun gettingLocation() {

        Handler().postDelayed({
            val gpsTracker = GPSTracker(context)
            gpsTracker.getLongitude()

            if (gpsTracker.getLatitude() == 0.0) {
                gettingLocation()
            } else {
                var latitude = gpsTracker.getLatitude().toString()
                var longitude = gpsTracker.getLongitude().toString()
                callApi(latitude, longitude)
            }
        }, 2000)
    }
*/

    private fun callApi(lat: String, logitutde: String) {
        val locationRequestModel = LocationRequestModel()

        var locations = locationRequestModel.Locations()

        locations.latitude = lat
        locations.longitude = logitutde
        var currentDat = ""
        try {
            val sdf = SimpleDateFormat("yyyy/M/dd hh:mm:ss")
            currentDat = sdf.format(Date()).toString()
        } catch (e: Exception) {
        }
        locations.time = currentDat
        var locationArr: ArrayList<LocationRequestModel.Locations> = arrayListOf()
        locationArr.add(locations)
        locationRequestModel.locations = locationArr

        ApiClient.updateLiveLocation(StaticSharedpreference.getInfo(
            Constant.ACCESS_TOKEN,
            applicationContext
        ).toString(), locationRequestModel, object : APIResultLitener<JsonObject> {
            override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                if (response != null && errorMessage == null) {

                    if (response.code() == 200) {

                        Toast.makeText(
                            applicationContext,
                            "success location update",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "error location update",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    //  dialog.dismiss()

                }
            }
        })

    }
}
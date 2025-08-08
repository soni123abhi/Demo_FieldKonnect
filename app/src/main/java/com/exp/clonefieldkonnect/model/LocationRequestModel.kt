package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocationRequestModel {

    @SerializedName("locations")
    @Expose
    var locations: ArrayList<Locations>? = null

    inner class Locations {

        @SerializedName("latitude")
        @Expose
        var latitude: String? = null

        @SerializedName("longitude")
        @Expose
        var longitude: String? = null

        @SerializedName("time")
        @Expose
        var time: String? = null

    }
}
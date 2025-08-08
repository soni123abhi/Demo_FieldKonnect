package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CityModel {

    @SerializedName("status")
    @Expose
    private val status: String? = null

    @SerializedName("message")
    @Expose
    private val message: String? = null

    @SerializedName("data")
    @Expose
    lateinit var  data: List<Datum>

    class Datum {
        @SerializedName("city_id")
        @Expose
        var city_id: Int? = null

        @SerializedName("city_name")
        @Expose
        var city_name: String? = null

    }
}
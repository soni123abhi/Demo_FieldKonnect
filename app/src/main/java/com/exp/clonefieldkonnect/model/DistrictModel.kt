package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class DistrictModel {

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
        @SerializedName("district_id")
        @Expose
        var district_id: Int? = null

        @SerializedName("district_name")
        @Expose
        var district_name: String? = null

    }
}
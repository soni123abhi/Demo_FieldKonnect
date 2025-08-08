package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PinCodeModel {

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
        @SerializedName("pincode_id")
        @Expose
        var pincode_id: Int? = null

        @SerializedName("pincode")
        @Expose
        var pincode : String? = null

    }
}
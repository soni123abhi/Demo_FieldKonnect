package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class StateModel {

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
        @SerializedName("state_id")
        @Expose
        var stateId: Int? = null

        @SerializedName("state_name")
        @Expose
        var stateName: String? = null

    }
}
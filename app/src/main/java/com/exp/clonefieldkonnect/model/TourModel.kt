package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class TourModel {

    @SerializedName("id")
    @Expose
     val id: Int? = null

     @SerializedName("date")
    @Expose
     val date: String? = null

    @SerializedName("userid")
    @Expose
     val userid: String? = null

    @SerializedName("objectives")
    @Expose
     val objectives: String? = null

    @SerializedName("town")
    @Expose
     val town: String? = null

    @SerializedName("type")
    @Expose
     val type: String? = null

    @SerializedName("status")
    @Expose
     val status: Any? = null


}
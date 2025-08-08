package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserActivityDetailModel {

     @SerializedName("title")
     @Expose
     var title: String? = null

     @SerializedName("time")
     @Expose
     var time: String? = null

     @SerializedName("latitude")
     @Expose
     var latitude: String? = null

     @SerializedName("longitude")
     @Expose
     var longitude: String? = null

     @SerializedName("msg")
     @Expose
     var msg: String? = null

     @SerializedName("date")
     @Expose
     var date: String? = null

 }
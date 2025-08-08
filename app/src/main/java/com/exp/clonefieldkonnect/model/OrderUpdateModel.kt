package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class OrderUpdateModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

 }
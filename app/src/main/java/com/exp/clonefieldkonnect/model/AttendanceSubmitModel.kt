package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class AttendanceSubmitModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

    @SerializedName("checkin_id")
     var checkin_id: Int? = null

 }
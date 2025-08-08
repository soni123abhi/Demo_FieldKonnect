package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


 class MspActivityTypeModel {

     @SerializedName("status")
     var status: Boolean? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()


     class Data {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("type")
         var type: String? = null

     }
 }
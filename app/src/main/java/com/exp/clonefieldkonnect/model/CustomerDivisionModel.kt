package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

 class CustomerDivisionModel {

     @SerializedName("status")
     var status: String? = null
     @SerializedName("message")
     var message: String? = null
     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()


     class Data {

         @SerializedName("id")
         var id: String? = null

         @SerializedName("title")
         var title: String? = null

     }
 }
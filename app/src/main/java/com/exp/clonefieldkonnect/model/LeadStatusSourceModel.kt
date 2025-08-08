package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class LeadStatusSourceModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()

     class Data {

         @SerializedName("status")
         var status: ArrayList<Status> = arrayListOf()

         @SerializedName("source")
         var source: ArrayList<Source> = arrayListOf()

     }

     class Status {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("display_name")
         var displayName: String? = null

     }

     class Source {

         @SerializedName("key")
         var key: String? = null

         @SerializedName("value")
         var value: String? = null

     }

 }
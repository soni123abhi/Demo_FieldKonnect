package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

 class VersionModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()


     class Data {

         @SerializedName("app_version")
         var appVersion: String? = null

         @SerializedName("media")
         var media      : ArrayList<Media> = arrayListOf()
     }

      class Media {

          @SerializedName("name")
          var name: String? = null

          @SerializedName("original_url")
          var original_url: String? = null

      }
 }
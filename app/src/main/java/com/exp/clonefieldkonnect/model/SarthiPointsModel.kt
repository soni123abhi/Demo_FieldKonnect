package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class SarthiPointsModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()

     class Data {

         @SerializedName("total_points")
         var totalPoints: String? = null

         @SerializedName("active_points")
         var activePoints: Int? = null

         @SerializedName("provision_points")
         var provisionPoints: Int? = null

         @SerializedName("total_redemption")
         var totalRedemption: Int? = null

         @SerializedName("total_rejected")
         var totalRejected: Int? = null

         @SerializedName("total_balance")
         var totalBalance: Int? = null

     }

 }
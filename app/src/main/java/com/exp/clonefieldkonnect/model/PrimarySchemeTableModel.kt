package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class PrimarySchemeTableModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()


     class Data {

         @SerializedName("dealer_name")
         var dealerName: String? = null

         @SerializedName("group_name")
         var groupName: String? = null

         @SerializedName("sale_qty")
         var saleQty: String? = null

         @SerializedName("sale_amount")
         var saleAmount: String? = null

         @SerializedName("discount_cn")
         var discountCn: Double? = null

     }

 }
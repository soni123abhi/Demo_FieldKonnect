package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

 class SalessListModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: ArrayList<sales_Data> = arrayListOf()

     @SerializedName("all_status")
     var allStatus: ArrayList<AllStatus> = arrayListOf()

     class sales_Data {

         @SerializedName("sales_id")
         var salesId: Int? = null

         @SerializedName("buyer_id")
         var buyerId: Int? = null

         @SerializedName("buyer_name")
         var buyerName: String? = null

         @SerializedName("seller_id")
         var sellerId: Int? = null

         @SerializedName("seller_name")
         var sellerName: String? = null

         @SerializedName("order_id")
         var orderId: Int? = null

         @SerializedName("grand_total")
         var grandTotal: String? = null

         @SerializedName("invoice_date")
         var invoiceDate: String? = null

         @SerializedName("invoice_no")
         var invoiceNo: String? = null

         @SerializedName("quantity")
         var quantity: String? = null

         @SerializedName("status")
         var status: String? = null

         @SerializedName("point_type")
         var pointType: String? = null

         @SerializedName("orderno")
         var orderno: String? = null

         @SerializedName("sub_total")
         var sub_total: String? = null

         @SerializedName("createdbyname")
         var createdbyname: String? = null

     }

     class AllStatus {

         @SerializedName("id")
         var id: String? = null

         @SerializedName("name")
         var name: String? = null

     }
 }
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class OrderListModel {


    @SerializedName("status")
    @Expose
    private val status: String? = null

    @SerializedName("message")
    @Expose
    private val message: String? = null

    @SerializedName("data")
    @Expose
     lateinit var data: ArrayList<Datum?>

    @SerializedName("users")
    var users : ArrayList<Users> = arrayListOf()

    @SerializedName("all_status")
    var allStatus : ArrayList<AllStatus> = arrayListOf()


    class Users {
         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null
     }

     class AllStatus {
         @SerializedName("id")
         var id: String? = null

         @SerializedName("name")
         var name: String? = null
     }

    class Datum {
        @SerializedName("order_id")
        @Expose
        var orderId: Int? = null

        @SerializedName("buyer_name")
        @Expose
        var buyerName: String? = null


        @SerializedName("seller_id")
        @Expose
        var sellerId: String? = null

        @SerializedName("seller_name")
        @Expose
        var sellerName: String? = null

        @SerializedName("total_qty")
        @Expose
        var totalQty: String? = null

        @SerializedName("shipped_qty")
        @Expose
        var shippedQty: String? = null

        @SerializedName("orderno")
        @Expose
        var orderno: String? = null

        @SerializedName("order_date")
        @Expose
        var orderDate: String? = null

        @SerializedName("completed_date")
        @Expose
        var completedDate: String? = null

        @SerializedName("grand_total")
        @Expose
        var grandTotal: String? = null

        @SerializedName("sub_total")
        @Expose
        var subTotal: String? = null

        @SerializedName("order_status")
        @Expose
        var order_status: String? = null

        @SerializedName("order_status_id")
        @Expose
        var order_status_id: String? = null

        @SerializedName("creatd_by")
        @Expose
        var creatd_by: String? = null

    }
}
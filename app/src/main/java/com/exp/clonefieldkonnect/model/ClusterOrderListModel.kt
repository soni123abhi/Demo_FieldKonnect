package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class ClusterOrderListModel {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("data")
    var data: ArrayList<Datum?> = arrayListOf()

    @SerializedName("all_users")
    var users: ArrayList<Users> = arrayListOf()

    @SerializedName("all_status")
    var allStatus: ArrayList<AllStatus> = arrayListOf()


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
        var orderId: Int? = null

        @SerializedName("seller_id")
        var sellerId: Int? = null

        @SerializedName("seller_name")
        var sellerName: String? = null

        @SerializedName("buyer_id")
        var buyerId: Int? = null

        @SerializedName("buyer_name")
        var buyerName: String? = null

        @SerializedName("total_qty")
        var totalQty: Int? = null

        @SerializedName("shipped_qty")
        var shippedQty: Int? = null

        @SerializedName("orderno")
        var orderno: String? = null

        @SerializedName("order_date")
        var orderDate: String? = null

        @SerializedName("completed_date")
        var completedDate: String? = null

        @SerializedName("grand_total")
        var grandTotal: String? = null

        @SerializedName("sub_total")
        var subTotal: String? = null

        @SerializedName("discount_status")
        var discountStatus: String? = null

    }
}
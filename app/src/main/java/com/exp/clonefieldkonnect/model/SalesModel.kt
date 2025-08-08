package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class SalesModel {

    @SerializedName("status")
    @Expose
     val status: String? = null

    @SerializedName("message")
    @Expose
     val message: String? = null

    @SerializedName("data")
    @Expose
    lateinit var data: ArrayList<Datum?>

    class Datum {
        @SerializedName("seller_id")
        @Expose
        var sellerId: String? = null

        @SerializedName("seller_name")
        @Expose
        var sellerName: String? = null

        @SerializedName("order_id")
        @Expose
        var orderId: Int? = null

        @SerializedName("grand_total")
        @Expose
        var grandTotal: String? = null

        @SerializedName("invoice_date")
        @Expose
        var invoiceDate: String? = null

        @SerializedName("sales_id")
        @Expose
        var salesId: Int? = null


        @SerializedName("buyer_name")
        @Expose
        var buyerName: String? = null

        @SerializedName("status_id")
        @Expose
        var status_id: String? = null

        @SerializedName("status_name")
        @Expose
        var status_name: String? = null


        @SerializedName("invoice_no")
        @Expose
        var invoiceNo: String? = null

        @SerializedName("points")
        @Expose
        var points: Int? = null

        @SerializedName("point_type")
        @Expose
        var pointType: String? = null

        @SerializedName("invoice_image")
        @Expose
        var invoiceImage: String? = null

    }
}
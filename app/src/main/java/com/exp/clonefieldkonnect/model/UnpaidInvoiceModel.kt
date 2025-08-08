package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class UnpaidInvoiceModel {

    @SerializedName("status")
    @Expose
     val status: String? = null

    @SerializedName("message")
    @Expose
     val message: String? = null

    @SerializedName("data")
    @Expose
     val data: ArrayList<Datum>? = null


    class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("invoice_date")
        @Expose
        var invoiceDate: String? = null

        @SerializedName("invoice_no")
        @Expose
        var invoiceNo: String? = null

        @SerializedName("grand_total")
        @Expose
        var grandTotal: String? = null

        @SerializedName("amount_unpaid")
        @Expose
        var amountUnpaid: Float? = null


        var enterAmount: String = ""

        @SerializedName("order_id")
        @Expose
        var orderId: Int? = null

        @SerializedName("status_id")
        @Expose
        var statusId: Int? = null

    }
}
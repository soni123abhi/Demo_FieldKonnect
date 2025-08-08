package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PaymentReceivedRequestModel {

    @SerializedName("customer_id")
    @Expose
    var customer_id: String? = null

    @SerializedName("payment_mode")
    @Expose
    var payment_mode: String? = null


    @SerializedName("payment_type")
    @Expose
    var payment_type: String? = null

 @SerializedName("payment_date")
    @Expose
    var payment_date: String? = null


    @SerializedName("amount")
    @Expose
    var amount: String? = null


    @SerializedName("description")
    @Expose
    var description: String? = null


    @SerializedName("reference_no")
    @Expose
    var reference_no: String? = null


    @SerializedName("bank_name")
    @Expose
    var bank_name: String? = null


    @SerializedName("detail")
    @Expose
    var detail: List<Datum>? = null


    inner class Datum {

        @SerializedName("sales_id")
        @Expose
        var sales_id: String? = null

        @SerializedName("invoice_no")
        @Expose
        var invoice_no: String? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

    }
}
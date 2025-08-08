package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PaymentReceivedSalesArrRequestModel {

/*

    var detail: List<Datum>? = null


    inner class Datum {*/

        @SerializedName("sales_id")
        @Expose
        var sales_id: String? = null

        @SerializedName("invoice_no")
        @Expose
        var invoice_no: String? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

   // }
}
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class InsertSalesRequestModel {

    @SerializedName("buyer_id")
    @Expose
    var buyer_id: String? = null

    @SerializedName("seller_id")
    @Expose
    var seller_id: String? = null


    @SerializedName("order_id")
    @Expose
    var order_id: String? = null


    @SerializedName("invoice_no")
    @Expose
    var invoice_no: String? = null


    @SerializedName("invoice_date")
    @Expose
    var invoice_date: String? = null

    @SerializedName("grand_total")
    @Expose
    var grand_total: String? = null

    @SerializedName("images")
    @Expose
    var data: List<Datum>? = null


  inner  class Datum {

        var images: String? = null


    }
}
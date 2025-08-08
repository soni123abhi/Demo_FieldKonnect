package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OutstandingModel {

    @SerializedName("status")
    @Expose
     val status: String? = null

    @SerializedName("message")
    @Expose
     val message: String? = null

    @SerializedName("data")
    @Expose
     val data: ArrayList<Data>? = null

    class Data {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("customer_id")
        @Expose
        var customer_id: Int? = null

        @SerializedName("payment_date")
        @Expose
        var payment_date: String? = null

        @SerializedName("payment_type")
        @Expose
        var payment_type: String? = null

        @SerializedName("reference_no")
        @Expose
        var reference_no: String? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

        @SerializedName("payment_mode")
        @Expose
        var payment_mode: Any? = null

        @SerializedName("bank_name")
        @Expose
        var bank_name: String? = null


        }

}
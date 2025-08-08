package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PointCollectionModel {

    @SerializedName("id")
    @Expose
     val id: Int? = null

    @SerializedName("customer_id")
    @Expose
     val customerId: Int? = null

    @SerializedName("points")
    @Expose
     val points: Int? = null

    @SerializedName("point_type")
    @Expose
     val pointType: String? = null

    @SerializedName("transaction_at")
    @Expose
     val transactionAt: String? = null

    @SerializedName("checkinid")
    @Expose
     val checkinid: Int? = null

    @SerializedName("quantity")
    @Expose
     val quantity: Int? = null

    @SerializedName("customers")
    @Expose
     val customers: Customers? = null

    class Customers {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("first_name")
        @Expose
        var firstName: String? = null

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null

        @SerializedName("customertype")
        @Expose
        var customertype: Int? = null

        @SerializedName("mobile")
        @Expose
        var mobile: String? = null
    }
}
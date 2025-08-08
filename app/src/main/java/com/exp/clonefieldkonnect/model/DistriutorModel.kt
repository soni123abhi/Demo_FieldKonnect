package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class DistriutorModel {

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
        @SerializedName("seller_id")
        @Expose
        var sellerId: Int? = null

       @SerializedName("customer_id")
        @Expose
        var customer_id: Int? = null

        @SerializedName("seller_name")
        @Expose
        var sellerName: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("first_name")
        @Expose
        var firstName: String? = null

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null

        @SerializedName("profile_image")
        @Expose
        var profileImage: String? = null

        @SerializedName("mobile")
        @Expose
        var mobile: String? = null

        @SerializedName("customer_type")
        @Expose
        var customer_type: String? = null

    }
}
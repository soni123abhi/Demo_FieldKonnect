package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class BeatCustomerModel {


    @SerializedName("beat_detail_id")
    @Expose
    var beatDetailId: Int? = null

    @SerializedName("customer_id")
    @Expose
    var customerId: String? = null

    @SerializedName("name")
    @Expose
    var customerName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("mobile")
    @Expose
    var customerMobile: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("beat_name")
    @Expose
    var beatName: String? = null

    @SerializedName("beat_date")
    @Expose
    var beatDate: String? = null

    @SerializedName("address1")
    @Expose
    var address: String? = null

    @SerializedName("profile_image")
    @Expose
    var profile_image: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("outstanding")
    @Expose
    var outstanding: String? = null
    @SerializedName("grade")
    @Expose
    var grade: String? = null
    @SerializedName("visit_status")
    @Expose
    var visitStatus: String? = null
}
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class StoreCustomerRequestModel {

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("full_name")
    @Expose
    var full_name: String? = null

    @SerializedName("first_name")
    @Expose
    var first_name: String? = null

    @SerializedName("last_name")
    @Expose
    var last_name: String? = null

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("address1")
    @Expose
    var address1: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("beat_id")
    @Expose
    var beat_id: String? = null

    @SerializedName("gstin_no")
    @Expose
    var gstin_no: String? = null

    @SerializedName("pan_no")
    @Expose
    var pan_no: String? = null

    @SerializedName("aadhar_no")
    @Expose
    var aadhar_no: String? = null

    @SerializedName("otherid_no")
    @Expose
    var otherid_no: String? = null

    @SerializedName("gstin_image")
    @Expose
    var gstin_image: String? = null

    @SerializedName("pan_image")
    @Expose
    var pan_image: String? = null

    @SerializedName("aadhar_image")
    @Expose
    var aadhar_image: String? = null

    @SerializedName("other_image")
    @Expose
    var other_image: String? = null

    @SerializedName("zipcode")
    @Expose
    var zipcode: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

   @SerializedName("visiting_card")
    @Expose
    var visitingCard: String? = null


    @SerializedName("bank_name")
    @Expose
    var bank_name: String? = null


    @SerializedName("account_holder_name")
    @Expose
    var account_holder_name: String? = null


    @SerializedName("account_no")
    @Expose
    var account_no: String? = null


    @SerializedName("ifsc_code")
    @Expose
    var ifsc_code: String? = null

    @SerializedName("bank_passbook")
    @Expose
    var bank_passbook: String? = null

    @SerializedName("landmark")
    @Expose
    var landmark: String? = null

     @SerializedName("customertype")
    @Expose
    var customertype: String? = null

     @SerializedName("status_type")
    @Expose
    var statusType: String? = null

     @SerializedName("grade")
    @Expose
    var grade: String? = null

    @SerializedName("customer_id")
    @Expose
    var customer_id: String? = null


    @SerializedName("address_id")
    @Expose
    var address_id: String? = null


    @SerializedName("survey")
    @Expose
    var survey: ArrayList<Survey> = arrayListOf()

    inner class Survey {

        @SerializedName("field_id")
        @Expose
        var field_id: String? = null


        @SerializedName("value")
        @Expose
        var value: String? = null


    }


}
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class LeadModel {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("notification_count")
    var notification_count: String? = null

    @SerializedName("data")
    var data: ArrayList<Data_lead> = arrayListOf()

    @SerializedName("counts")
    var counts: ArrayList<Counts> = arrayListOf()

    class Data_lead {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("address")
        var address: String? = null

        @SerializedName("city")
        var city: String? = null

        @SerializedName("status")
        var status: Status? = Status()

        @SerializedName("contact")
        var contact: Contact? = Contact()

        @SerializedName("note")
        var note: String? = null

        @SerializedName("opportunity_status")
        var opportunity_status: String? = null

        @SerializedName("created_at")
        var createdAt: String? = null

    }

    class Status {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("display_name")
        var displayName: String? = null

    }

    class Contact {

        @SerializedName("name")
        var name: String? = null

        @SerializedName("phone_number")
        var phoneNumber: String? = null

        @SerializedName("email")
        var email: String? = null

        @SerializedName("url")
        var url: String? = null

        @SerializedName("lead_source")
        var leadSource: String? = null

    }

    class Counts {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("display_name")
        var displayName: String? = null

        @SerializedName("count")
        var count: Int? = null

    }
}
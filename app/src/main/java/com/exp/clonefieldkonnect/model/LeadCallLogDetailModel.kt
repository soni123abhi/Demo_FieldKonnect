package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class LeadCallLogDetailModel {

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: ArrayList<Data_log> = arrayListOf()

    @SerializedName("users")
    var users: ArrayList<Users> = arrayListOf()

    @SerializedName("call_dialted")
    var callDialted: Int? = null

    @SerializedName("connected")
    var connected: Int? = null

    @SerializedName("no_response")
    var noResponse: Int? = null

    @SerializedName("total_duration")
    var totalDuration: String? = null

    class User {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("name")
        var name: String? = null

    }

    class Contacts {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("lead_id")
        var leadId: Int? = null

        @SerializedName("name")
        var name: String? = null

    }

    class Lead {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("company_name")
        var companyName: String? = null

        @SerializedName("contacts")
        var contacts: ArrayList<Contacts> = arrayListOf()

    }

    class Data_log {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("lead_id")
        var leadId: Int? = null

        @SerializedName("number")
        var number: String? = null

        @SerializedName("started_at")
        var startedAt: String? = null

        @SerializedName("duration")
        var duration: String? = null

        @SerializedName("user_id")
        var userId: Int? = null

        @SerializedName("status")
        var status: Int? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("updated_at")
        var updatedAt: String? = null

        @SerializedName("contact_name")
        var contactName: String? = null

        @SerializedName("user")
        var user: User? = User()

        @SerializedName("lead")
        var lead: Lead? = Lead()

    }

    class Users {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("name")
        var name: String? = null

    }
}
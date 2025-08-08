package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class TourViewDetailModel {
        @SerializedName("id")
        var id: Int? = null

        @SerializedName("date")
        var date: String? = null

        @SerializedName("userid")
        var userid: String? = null

        @SerializedName("town")
        var town: String? = null

        @SerializedName("objectives")
        var objectives: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("self")
        var self: String? = null

        @SerializedName("created_by")
        var createdBy: String? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("updated_at")
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        var deletedAt: String? = null

        @SerializedName("isSelected")
        var isSelected: Boolean? = false


    }


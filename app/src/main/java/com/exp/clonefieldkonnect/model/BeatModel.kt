package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class BeatModel {

    @SerializedName("beat_id")
    @Expose
    val beatId: Int? = null

    @SerializedName("beat_name")
    @Expose
    val beatName: String? = null

    @SerializedName("beat_date")
    @Expose
    val beatDate: String? = null

    @SerializedName("description")
    @Expose
    val description: String? = null

    @SerializedName("total_customers")
    @Expose
    val total_customers: Int? = null

    @SerializedName("visited_customers")
    @Expose
    val visited_customers: Int? = null

    @SerializedName("remaining_customers")
    @Expose
    val remaining_customers: Int? = null

    @SerializedName("beatscheduleid")
    @Expose
    val beatscheduleid: Int? = null

    @SerializedName("is_today")
    @Expose
    val is_today: Boolean = false

    var isChecked = false
}
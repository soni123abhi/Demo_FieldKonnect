package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class UserCityModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("city_name")
    @Expose
    var city_name: String? = null

    @SerializedName("grade")
    @Expose
    var grade: String? = null

    var isSelected = false
}
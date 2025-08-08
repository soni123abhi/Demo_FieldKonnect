package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class CustomerTypeModel {

    @SerializedName("customertype")
    @Expose
    var customertype: Int? = null

    @SerializedName("customertype_name")
    @Expose
    var customertype_name: String? = null

}
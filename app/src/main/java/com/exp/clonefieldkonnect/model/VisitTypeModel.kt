package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class VisitTypeModel {

    @SerializedName("type_id")
    @Expose
     val typeId: Int? = null

    @SerializedName("type_name")
    @Expose
     val typeName: String? = null

}
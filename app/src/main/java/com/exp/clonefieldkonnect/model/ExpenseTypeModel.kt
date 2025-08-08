package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class ExpenseTypeModel {

    @SerializedName("status")
    var status: String? = null
    @SerializedName("data")
    var data: ArrayList<Data> = arrayListOf()

    class Data {


        @SerializedName("id")
        var id: Int? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("rate")
        var rate: Double? = null

        @SerializedName("is_active")
        var isActive: Int? = null

        @SerializedName("allowance_type_id")
        var allowanceTypeId: Int? = null

        @SerializedName("payroll_id")
        var payroll_id: String? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("updated_at")
        var updatedAt: String? = null

    }
}
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

data class ExpenseApprovalSubmitModel (

    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null

)
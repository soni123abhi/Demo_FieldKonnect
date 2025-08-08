package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


data class LeaveBalanceModel (

    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : Data?   = Data()

)
data class Data (

    @SerializedName("leaveBalance" ) var leaveBalance : String? = null,
    @SerializedName("comb_off"     ) var combOff      : String? = null

)
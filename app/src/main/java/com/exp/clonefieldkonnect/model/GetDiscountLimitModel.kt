package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class GetDiscountLimitModel (

    @SerializedName("status"               ) var status             : String? = null,
    @SerializedName("order_discount_limit" ) var orderDiscountLimit : String? = null

)
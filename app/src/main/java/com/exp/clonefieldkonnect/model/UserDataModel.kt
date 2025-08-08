package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class UserDataModel(

    @SerializedName("status" ) var status : String? = null,
    @SerializedName("data"   ) var data   : UserData?   = UserData(),
    @SerializedName("Branches" ) var Branches : ArrayList<Branches> = arrayListOf(),
    @SerializedName("divisions" ) var divisions : ArrayList<Divisions> = arrayListOf()

)
data class UserData (

    @SerializedName("target"         ) var target        : String? = null,
    @SerializedName("achievement"    ) var achievement   : String? = null,
    @SerializedName("achiv_per"      ) var achivPer      : String? = null,
    @SerializedName("target_per"     ) var targetPer     : String? = null,
    @SerializedName("order_value"    ) var orderValue    : String? = null,
    @SerializedName("order_qty"      ) var orderQty      : String? = null,
    @SerializedName("customer_visit" ) var customerVisit : String? = null,
    @SerializedName("todayBeatSchedule" ) var todayBeatSchedule : Boolean? = null,
    @SerializedName("beatUser"         ) var beatUser          : Boolean? = null

)
data class Branches (

    @SerializedName("id"          ) var id         : Int?    = null,
    @SerializedName("branch_name" ) var branchName : String? = null

)
data class Divisions (

    @SerializedName("id"            ) var id           : Int?    = null,
    @SerializedName("division_name" ) var divisionName : String? = null

)
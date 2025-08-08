package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class DealerSalesReportModel {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("data")
    var data: ArrayList<Data> = arrayListOf()

    @SerializedName("users")
    var users: ArrayList<Users> = arrayListOf()

    @SerializedName("branches")
    var branches: ArrayList<Branches> = arrayListOf()

    @SerializedName("year_rang")
    var yearRang: ArrayList<YearRang> = arrayListOf()

    @SerializedName("ps_divisions" )
    var psDivisions : ArrayList<String>   = arrayListOf()



    class Data {

     @SerializedName("dealer")
     var dealer: String? = null

     @SerializedName("total_net_amount_last_year")
     var totalNetAmountLastYear: String? = null

     @SerializedName("total_quantity_last_year")
     var totalQuantityLastYear: String? = null

     @SerializedName("total_net_amount_current_year")
     var totalNetAmountCurrentYear: String? = null

     @SerializedName("total_quantity_current_year")
     var totalQuantityCurrentYear: String? = null

     @SerializedName("total_net_amount_last_month")
     var totalNetAmountLastMonth: String? = null

     @SerializedName("total_quantity_last_month")
     var totalQuantityLastMonth: String? = null

     @SerializedName("total_net_amount_current_month")
     var totalNetAmountCurrentMonth: String? = null

     @SerializedName("total_quantity_current_month")
     var totalQuantityCurrentMonth: String? = null

 }
 class Users {

     @SerializedName("dealer")
     var dealer: String? = null

     @SerializedName("id")
     var id: Int? = null

 }

 class Branches {

     @SerializedName("final_branch")
     var finalBranch: String? = null

     @SerializedName("id")
     var id: Int? = null

 }
 class YearRang {
     @SerializedName("range")
     var range: String? = null
 }
}

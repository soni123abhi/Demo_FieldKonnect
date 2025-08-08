package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class DealergrowthModel {

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

     @SerializedName("currentYear")
     var currentYear: Int? = null

     @SerializedName("ps_divisions")
     var psDivisions: ArrayList<String> = arrayListOf()

    @SerializedName("remarks")
    var remarks: ArrayList<Remarks>  = arrayListOf()


    class Data {

          @SerializedName("dealer")
          var dealer: String? = null

          @SerializedName("final_branch")
          var finalBranch: String? = null

          @SerializedName("city")
          var city: String? = null

          @SerializedName("total_net_amounts")
          var totalNetAmounts: String? = null

          @SerializedName("last_year_net_amounts")
          var lastYearNetAmounts: String? = null

          @SerializedName("goly")
          var goly: String? = null

          @SerializedName("remark")
          var remark: String? = null

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
     class Remarks {

         @SerializedName("id")
         var id: String? = null
         
         @SerializedName("title")
         var title: String? = null

     }

 }
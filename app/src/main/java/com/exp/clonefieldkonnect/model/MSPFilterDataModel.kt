package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class MSPFilterDataModel {

     @SerializedName("status")
     var status: Boolean? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("users")
     var users: ArrayList<Users> = arrayListOf()

     @SerializedName("financial_year")
     var financialYear: ArrayList<FinancialYear> = arrayListOf()

     @SerializedName("branches")
     var branches: ArrayList<Branches> = arrayListOf()


      class Users {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("name")
          var name: String? = null

          @SerializedName("employee_codes")
          var employeeCodes: String? = null

      }
      class FinancialYear {

          @SerializedName("year")
          var year: String? = null

      }
      class Branches {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("branch_name")
          var branchName: String? = null

      }


 }
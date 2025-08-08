package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

 class UserExpenseListModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()

     @SerializedName("expence_types")
     var expenceTypes : ArrayList<ExpenceTypes> = arrayListOf()

     @SerializedName("all_status")
     var allStatus    : ArrayList<AllStatus>    = arrayListOf()


      class AllStatus {

          @SerializedName("id")
          var id: String? = null
          @SerializedName("name")
          var name: String? = null

      }



     class ExpenceTypes {

          @SerializedName("id")
          var id: Int? = null
          @SerializedName("name")
          var name: String? = null

      }


     class Data {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("expenses_type")
         var expensesType: Int? = null

         @SerializedName("expenses_type_name")
         var expensesTypeName: String? = null

         @SerializedName("user_id")
         var userId: Int? = null

         @SerializedName("date")
         var date: String? = null

         @SerializedName("note")
         var note: String? = null

         @SerializedName("start_km")
         var startKm: String? = null

         @SerializedName("stop_km")
         var stopKm: String? = null

         @SerializedName("total_km")
         var totalKm: String? = null

         @SerializedName("claim_amount")
         var claimAmount: Double? = null

         @SerializedName("approve_amount")
         var approve_amount: String? = null

         @SerializedName("expense_image")
         var expenseImage : ArrayList<String> = arrayListOf()

         @SerializedName("status")
         var status: String? = null

     }
 }
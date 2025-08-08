package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class ReportcountModel {

     @SerializedName("status")
     var status: String? = null
     @SerializedName("message")
     var message: String? = null
     @SerializedName("data")
     var data: Data? = Data()


     class Data {

         @SerializedName("pending_attendance")
         var pendingAttendance: Int? = null

         @SerializedName("pending_tour_plan")
         var pendingTourPlan: Int? = null

         @SerializedName("pending_expense")
         var pendingExpense: Int? = null

         @SerializedName("pending_all_expense")
         var pendingallexpense: Int? = null

         @SerializedName("pending_order_discount")
         var pendingOrderDiscount: Int? = null

         @SerializedName("pending_orders")
         var pending_orders: Int? = null

         @SerializedName("pending_appointment")
         var pending_appointment: Int? = null

     }
 }
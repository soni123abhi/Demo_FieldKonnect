package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


 class UserExpenseDetailModel {

    @SerializedName("status"  ) var status  : String? = null
    @SerializedName("message" ) var message : String? = null
    @SerializedName("data"    ) var data    : Data?   = Data()

      class Data {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("expenses_type")
          var expensesType: Int? = null

          @SerializedName("expenses_type_name")
          var expensesTypeName: String? = null

          @SerializedName("rate")
          var rate: Double? = null

          @SerializedName("allowance_type_id")
          var allowanceTypeId: Int? = null

          @SerializedName("user_id")
          var userId: Int? = null

          @SerializedName("user_name")
          var userName: String? = null

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

          @SerializedName("expense_image")
          var expenseImage: ArrayList<String> = arrayListOf()

          @SerializedName("image_id")
          var imageId: ArrayList<Int> = arrayListOf()

          @SerializedName("approve_amount")
          var approveAmount: String? = null

          @SerializedName("status")
          var status: String? = null

          @SerializedName("reason")
          var reason: String? = null

          @SerializedName("plan")
          var plan: plan? = plan()

          @SerializedName("total_visit")
          var totalVisit: String? = null

          @SerializedName("total_dis")
          var totalDis: String? = null

      }

      class plan {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("date")
          var date: String? = null

          @SerializedName("userid")
          var userid: Int? = null

          @SerializedName("town")
          var town: String? = null

          @SerializedName("objectives")
          var objectives: String? = null

          @SerializedName("type")
          var type: String? = null

          @SerializedName("status")
          var status: Int? = null

          @SerializedName("created_by")
          var createdBy: String? = null

          @SerializedName("created_at")
          var createdAt: String? = null

          @SerializedName("updated_at")
          var updatedAt: String? = null

          @SerializedName("deleted_at")
          var deletedAt: String? = null

      }
 }
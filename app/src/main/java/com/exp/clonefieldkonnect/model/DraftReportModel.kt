package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


class DraftReportModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("data")
     var data: Data? = Data()

     @SerializedName("message")
     var message: String? = null

      class Data {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("checkin_id")
          var checkinId: Int? = null

          @SerializedName("draft_msg")
          var draftMsg: String? = null

          @SerializedName("created_at")
          var createdAt: String? = null

          @SerializedName("updated_at")
          var updatedAt: String? = null

      }

 }
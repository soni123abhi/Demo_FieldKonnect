package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


class BeatScheduleModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()

      class Data {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("active")
          var active: String? = null

          @SerializedName("beat_id")
          var beatId: Int? = null

          @SerializedName("beat_date")
          var beatDate: String? = null

          @SerializedName("user_id")
          var userId: Int? = null

          @SerializedName("created_at")
          var createdAt: String? = null

          @SerializedName("updated_at")
          var updatedAt: String? = null

          @SerializedName("tourid")
          var tourid: String? = null

          @SerializedName("beats")
          var beats: Beats? = Beats()

      }

      class Beats {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("active")
          var active: String? = null

          @SerializedName("beat_name")
          var beatName: String? = null

          @SerializedName("description")
          var description: String? = null

          @SerializedName("created_by")
          var createdBy: Int? = null

          @SerializedName("city_id")
          var cityId: String? = null

          @SerializedName("state_id")
          var stateId: String? = null

          @SerializedName("district_id")
          var districtId: String? = null

      }

 }
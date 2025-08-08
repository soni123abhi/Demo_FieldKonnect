package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


 class UserAttendanceListModel {

     @SerializedName("status")
     var status: String? = null
     @SerializedName("message")
     var message: String? = null
     @SerializedName("users")
     var users: ArrayList<Users> = arrayListOf()
     @SerializedName("branches")
     var branches: ArrayList<Branches> = arrayListOf()
     @SerializedName("page_count")
     var pageCount: Int? = null
     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()
     @SerializedName("all_status" )
     var allStatus : ArrayList<AllStatus> = arrayListOf()


     class Users {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

     class Branches {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

      class AllStatus {

          @SerializedName("id")
          var id: String? = null

          @SerializedName("name")
          var name: String? = null

      }

     class Data {

         @SerializedName("attendance_id")
         var attendanceId: Int? = null

         @SerializedName("name")
         var name: String? = null

         @SerializedName("date")
         var date: String? = null

         @SerializedName("punch_in")
         var punchIn: String? = null

         @SerializedName("punch_out")
         var punchOut: String? = null

         @SerializedName("status")
         var status: String? = null

         @SerializedName("self")
         var self: Boolean? = null

         var isChecked: Boolean = false

     }
 }
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

 class DealerApprovalListModel {

     @SerializedName("status")
     var status: String? = null
     @SerializedName("message")
     var message: String? = null
     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()
     @SerializedName("braches")
     var braches: ArrayList<Braches> = arrayListOf()
     @SerializedName("users")
     var users: ArrayList<Users> = arrayListOf()
     @SerializedName("all_status")
     var allStatus: ArrayList<AllStatus> = arrayListOf()


     class AllStatus {

         @SerializedName("id")
         var id: String? = null

         @SerializedName("name")
         var name: String? = null

     }

     class Users {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

     class Braches {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("branch_name")
         var branchName: String? = null

     }

     class Data {


         @SerializedName("id")
         var id : Int?    = null

         @SerializedName("branch")
         var branch : String? = null

         @SerializedName("district")
         var district  : String? = null

         @SerializedName("approval_status")
         var approvalStatus  : String? = null

         @SerializedName("created_by")
         var createdBy : String? = null

         @SerializedName("created_by_id")
         var created_by_id : Int? = null

         @SerializedName("appointment_date")
         var appointmentDate : String? = null

         @SerializedName("firm_name")
         var firmName  : String? = null

         @SerializedName("place")
         var place  : String? = null

         @SerializedName("division")
         var division  : String? = null

         @SerializedName("certificate")
         var certificate: String? = null

     }
 }
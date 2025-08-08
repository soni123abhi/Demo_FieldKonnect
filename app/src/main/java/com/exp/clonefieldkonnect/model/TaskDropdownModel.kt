package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class TaskDropdownModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()


     class Data {

         @SerializedName("users")
         var users: ArrayList<Users> = arrayListOf()

         @SerializedName("priorities")
         var priorities: ArrayList<Priorities> = arrayListOf()

         @SerializedName("status")
         var status: ArrayList<Status> = arrayListOf()

     }


     class Users {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }


     class Priorities {

         @SerializedName("id")
         var id: String? = null

         @SerializedName("name")
         var name: String? = null

     }


     class Status {

         @SerializedName("id")
         var id: String? = null

         @SerializedName("name")
         var name: String? = null

     }

 }
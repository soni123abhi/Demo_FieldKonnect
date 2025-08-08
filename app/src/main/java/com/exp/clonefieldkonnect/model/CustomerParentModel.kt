package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


 class CustomerParentModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()

     @SerializedName("current_page")
     var currentPage: Int? = null

     @SerializedName("total")
     var total: Int? = null

     @SerializedName("last_page")
     var lastPage: Int? = null

     @SerializedName("per_page")
     var perPage: Int? = null


     class Data {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

         var isselected = false


     }
 }
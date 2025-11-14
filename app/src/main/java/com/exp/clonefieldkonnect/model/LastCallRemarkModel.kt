package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


class LastCallRemarkModel {

     @SerializedName("success")
     var success: Boolean? = null

     @SerializedName("data")
     var data: Data? = Data()


     class Data {

         @SerializedName("last_call_id")
         var lastCallId: Int? = null

         @SerializedName("last_call_remark")
         var lastCallRemark: Boolean? = null

         @SerializedName("lead_type")
         var leadType: String? = null

         @SerializedName("lead_type_id")
         var leadTypeId: String? = null

         @SerializedName("all_types")
         var allTypes: ArrayList<AllTypes> = arrayListOf()

     }
     class AllTypes {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("display_name")
         var displayName: String? = null

     }

 }
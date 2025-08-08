package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class LeadContactModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()

     class Data {

         @SerializedName("contacts")
         var contacts: ArrayList<Contacts> = arrayListOf()

         @SerializedName("opportunity_statuses")
         var opportunityStatuses: ArrayList<OpportunityStatuses> = arrayListOf()

     }


     class OpportunityStatuses {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("status_name")
         var statusName: String? = null

     }


     class Contacts {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("lead_id")
         var leadId: Int? = null

         @SerializedName("name")
         var name: String? = null

         @SerializedName("title")
         var title: String? = null

         @SerializedName("phone_number")
         var phoneNumber: String? = null

         @SerializedName("email")
         var email: String? = null

         @SerializedName("lead_source")
         var leadSource: String? = null

         @SerializedName("url")
         var url: String? = null

         @SerializedName("created_by")
         var createdBy: Int? = null

         @SerializedName("created_at")
         var createdAt: String? = null

         @SerializedName("updated_at")
         var updatedAt: String? = null

     }

 }
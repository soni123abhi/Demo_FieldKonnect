package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


class OportunityDetailModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

    @SerializedName("notification_count")
     var notification_count: String? = null

     @SerializedName("data")
     var data: Data? = Data()


     class Data {

         @SerializedName("opportunities")
         var opportunities: ArrayList<Opportunities> = arrayListOf()

         @SerializedName("counter")
         var counter: ArrayList<Counter> = arrayListOf()

         @SerializedName("users")
         var users: ArrayList<Users> = arrayListOf()

     }

     class Users {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

     class Counter {

         @SerializedName("status_id")
         var statusId: Int? = null

         @SerializedName("status_name")
         var statusName: String? = null

         @SerializedName("total_opportunities")
         var totalOpportunities: Int? = null

         @SerializedName("total_amount")
         var totalAmount: Double? = null

     }

     class Opportunities {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("lead_id")
         var leadId: Int? = null

         @SerializedName("assigned_to")
         var assignedTo: Int? = null

         @SerializedName("lead_contact_id")
         var leadContactId: Int? = null

         @SerializedName("created_by")
         var createdBy: Int? = null

         @SerializedName("amount")
         var amount: Double? = null

         @SerializedName("type")
         var type: String? = null

         @SerializedName("estimated_close_date")
         var estimatedCloseDate: String? = null

         @SerializedName("confidence")
         var confidence: Int? = null

         @SerializedName("note")
         var note: String? = null

         @SerializedName("status")
         var status: String? = null

         @SerializedName("created_at")
         var createdAt: String? = null

         @SerializedName("updated_at")
         var updatedAt: String? = null

         @SerializedName("lead")
         var lead: Lead? = Lead()

         @SerializedName("assign_user")
         var assignUser: AssignUser? = AssignUser()

         @SerializedName("lead_contact")
         var leadContact: LeadContact? = LeadContact()

         @SerializedName("status_is")
         var statusIs: StatusIs? = StatusIs()

     }

     class LeadContact {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

     class StatusIs {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("status_name")
         var statusName: String? = null

     }


     class AssignUser {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

     class Lead {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("company_name")
         var companyName: String? = null

     }

 }
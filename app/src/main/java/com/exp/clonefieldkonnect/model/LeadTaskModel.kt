package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class LeadTaskModel {

     @SerializedName("status")
     var status: String? = null

    @SerializedName("notification_count")
     var notification_count: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()

     @SerializedName("message")
     var message: String? = null


     class Data {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("lead_id")
         var leadId: Int? = null

         @SerializedName("assigned_to")
         var assignedTo: Int? = null

         @SerializedName("created_by")
         var createdBy: Int? = null

         @SerializedName("description")
         var description: String? = null

         @SerializedName("date")
         var date: String? = null

         @SerializedName("time")
         var time: String? = null

         @SerializedName("priority")
         var priority: String? = null

         @SerializedName("status")
         var status: String? = null

         @SerializedName("open_date")
         var openDate: String? = null

         @SerializedName("due_date")
         var dueDate: String? = null

         @SerializedName("close_date")
         var closeDate: String? = null

         @SerializedName("remark")
         var remark: String? = null

         @SerializedName("created_at")
         var createdAt: String? = null

         @SerializedName("updated_at")
         var updatedAt: String? = null

         @SerializedName("created_at_formatted")
         var createdAtFormatted: String? = null

         @SerializedName("lead")
         var lead: Lead? = Lead()

         @SerializedName("assign_user")
         var assignUser: AssignUser? = AssignUser()

         @SerializedName("contact")
         var contact: Contact? = Contact()


     }

     class Contact {

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


     class Lead {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("company_name")
         var companyName: String? = null

     }
     class AssignUser {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

 }
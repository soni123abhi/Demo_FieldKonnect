package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class LeadDetailModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("notification_count")
     var notification_count: String? = null

     @SerializedName("data")
     var data: Data? = Data()

    @SerializedName("notes_tasks" )
    var notesTasks : ArrayList<NotesTasks> = arrayListOf()


     class NotesTasks {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("lead_id")
         var leadId: Int? = null

         @SerializedName("assigned_to")
         var assignedTo: Int? = null

         @SerializedName("message")
         var message: String? = null

         @SerializedName("description")
         var description: String? = null

         @SerializedName("note")
         var note: String? = null

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

         @SerializedName("type")
         var type: String? = null

         @SerializedName("created_at_formatted")
         var createdAtFormatted: String? = null


         @SerializedName("assignUser")
         var assignUse: AssignUser? = AssignUser()

         @SerializedName("createdby")
         var createdby: Createdby? = Createdby()


     }

     class Createdby {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }
     class AssignUser {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

    class Data {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("status_id")
         var status_id: Int? = null

         @SerializedName("pincode_id")
         var pincode_id: Int? = null

         @SerializedName("city_id")
         var city_id: Int? = null

         @SerializedName("district_id")
         var district_id: Int? = null

         @SerializedName("state_id")
         var state_id: Int? = null

        @SerializedName("assign_user_id")
         var assign_user_id: Int? = null

         @SerializedName("assign_user_name")
         var assign_user_name: String? = null

        @SerializedName("company_name")
         var companyName: String? = null

         @SerializedName("contact_id")
         var contactId: Int? = null

         @SerializedName("contact_name")
         var contactName: String? = null

         @SerializedName("website")
         var website: String? = null

         @SerializedName("phone_number")
         var phoneNumber: String? = null

         @SerializedName("email")
         var email: String? = null

         @SerializedName("address")
         var address: String? = null

         @SerializedName("pincode")
         var pincode: String? = null

         @SerializedName("city")
         var city: String? = null

         @SerializedName("district")
         var district: String? = null

         @SerializedName("state")
         var state: String? = null

         @SerializedName("status")
         var status: String? = null

         @SerializedName("lead_source")
         var leadSource: String? = null

         @SerializedName("note")
         var note: String? = null

         @SerializedName("lead_generation_date")
         var leadGenerationDate: String? = null

        @SerializedName("conversion_date")
         var conversion_date: String? = null

         @SerializedName("updated_at")
         var updatedAt: String? = null

     }

 }
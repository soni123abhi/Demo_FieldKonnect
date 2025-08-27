package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class TaskManagemnetModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()

     @SerializedName("notification_count")
     var notificationCount: Int? = null

     @SerializedName("message")
     var message: String? = null


     class Data {

         @SerializedName("id")
         var id: Int? = null


         @SerializedName("user_id")
         var userId: Int? = null


         @SerializedName("descriptions")
         var descriptions: String? = null


         @SerializedName("task_type")
         var taskType: String? = null

         @SerializedName("title")
         var title: String? = null


         @SerializedName("due_datetime")
         var dueDatetime: String? = null


         @SerializedName("completed_at")
         var completedAt: String? = null

         @SerializedName("completed")
         var completed: Int? = null

         @SerializedName("status_id")
         var statusId: String? = null

         @SerializedName("task_status")
         var taskStatus: String? = null

         @SerializedName("users")
         var users: Users? = Users()

         @SerializedName("task_department")
         var taskdepartment: Users? = Users()

         @SerializedName("task_priority")
         var taskpriority: Users? = Users()

         @SerializedName("lead")
         var lead: lead? = lead()

         @SerializedName("project")
         var project: project? = project()

         @SerializedName("customers")
         var customers: project? = project()

     }


     class project {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

    class lead {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("company_name")
         var companyname: String? = null

     }

    class Users {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("name")
         var name: String? = null

     }

 }


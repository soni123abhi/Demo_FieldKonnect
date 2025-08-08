package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TaskModel : Serializable{


    @SerializedName("id")
    @Expose
     val id: Int? = null

    @SerializedName("title")
    @Expose
     val title: String? = null

    @SerializedName("descriptions")
    @Expose
     val descriptions: String? = null

    @SerializedName("remark")
    @Expose
     val remark: String? = null

    @SerializedName("datetime")
    @Expose
     val datetime: String? = null

    @SerializedName("reminder")
    @Expose
     val reminder: Any? = null

    @SerializedName("completed_at")
    @Expose
     val completedAt: Any? = null

    @SerializedName("completed")
    @Expose
     val completed: Int? = null

    @SerializedName("is_done")
    @Expose
     val isDone: Int? = null

    @SerializedName("customer_id")
    @Expose
     val customerId: Int? = null

    @SerializedName("status_id")
    @Expose
     val statusId: Any? = null

    @SerializedName("customers")
    @Expose
     val customers: Customers? = null

    class Customers : Serializable {
        @SerializedName("id")
        @Expose
        var id: Int? = 0

        @SerializedName("profile_image")
        @Expose
        var profileImage: String? = ""

        @SerializedName("active")
        @Expose
        var active: String? = ""

        @SerializedName("name")
        @Expose
        var name: String? = ""

        @SerializedName("mobile")
        @Expose
        var mobile: String? = ""
    }
}
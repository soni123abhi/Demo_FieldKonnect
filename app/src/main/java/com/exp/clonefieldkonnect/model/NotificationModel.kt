package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


class NotificationModel {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("data")
    var data: ArrayList<Data> = arrayListOf()

    @SerializedName("message")
    var message: String? = null


     class Data {

         @SerializedName("id")
         var id: Int? = null

         @SerializedName("model_id")
         var modelId: Int? = null

         @SerializedName("user_id")
         var userId: Int? = null

         @SerializedName("title")
         var title: String? = null

         @SerializedName("body")
         var body: String? = null

         @SerializedName("model")
         var model: String? = null

         @SerializedName("read")
         var read: Int? = null

         @SerializedName("created_at")
         var createdAt: String? = null

         @SerializedName("updated_at")
         var updatedAt: String? = null

         @SerializedName("deleted_at")
         var deletedAt: String? = null

     }

}
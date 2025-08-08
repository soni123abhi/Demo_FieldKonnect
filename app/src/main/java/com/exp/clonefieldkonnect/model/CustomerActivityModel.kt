package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CustomerActivityModel () : Serializable {

     @SerializedName("id")
     var id: Int? = null

     @SerializedName("user_id")
     var userId: Int? = null

     @SerializedName("customer_id")
     var customerId: Int? = null

     @SerializedName("description")
     var description: String? = null

     @SerializedName("report_title")
     var reportTitle: String? = null

     @SerializedName("visit_image")
     var visitImage: String? = null

     @SerializedName("created_at")
     var createdAt: String? = null

     @SerializedName("user_name")
     var user_name: String? = null

 }

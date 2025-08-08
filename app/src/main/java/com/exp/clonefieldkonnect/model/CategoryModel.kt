package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CategoryModel {

    @SerializedName("id")
    @Expose
     val id: Int? = null

    @SerializedName("category_name")
    @Expose
     val categoryName: String? = null

    @SerializedName("category_image")
    @Expose
     val categoryImage: String? = null
     var selected: Boolean = false


}
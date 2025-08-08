package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class SubCategoryModel {


    @SerializedName("id")
    @Expose
     val id: Int? = null

    @SerializedName("subcategory_name")
    @Expose
     val subcategoryName: String? = null

    @SerializedName("subcategory_image")
    @Expose
     val subcategoryImage: String? = null

    @SerializedName("category_id")
    @Expose
     val categoryId: Int? = null

    @SerializedName("category_name")
    @Expose
     val categoryName: String? = null

    var selected = false
}
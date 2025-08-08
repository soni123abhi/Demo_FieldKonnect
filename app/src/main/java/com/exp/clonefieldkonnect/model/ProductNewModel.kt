package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ProductNewModel {

    @SerializedName("id")
    @Expose
     var id: Int? = null

    @SerializedName("product_name")
    @Expose
     var productName: String? = null

    @SerializedName("display_name")
    @Expose
     var displayName: String? = null

    @SerializedName("description")
    @Expose
     var description: String? = null

    @SerializedName("product_image")
    @Expose
     var productImage: String? = null

    @SerializedName("subcategory_id")
    @Expose
     var subcategoryId: Int? = null

    @SerializedName("subcategory_name")
    @Expose
     var subcategoryName: String? = null

    @SerializedName("category_id")
    @Expose
     var categoryId: Int? = null

    @SerializedName("category_name")
    @Expose
     var categoryName: String? = null

    @SerializedName("brand_id")
    @Expose
     var brandId: Int? = null

    @SerializedName("brand_name")
    @Expose
     var brandName: String? = null

    @SerializedName("unit_id")
    @Expose
     var unitId: Int? = null

    @SerializedName("unit_code")
    @Expose
     var unitCode: String? = null

    @SerializedName("detail_id")
    @Expose
     var detailId: Int? = null

    @SerializedName("mrp")
    @Expose
     var mrp: String? = null

    @SerializedName("price")
    @Expose
     var price: String? = null

    @SerializedName("selling_price")
    @Expose
     var sellingPrice: String? = null

    @SerializedName("specification")
    @Expose
     var specification: String? = null

    @SerializedName("hp")
    @Expose
     var hp: String? = null

    @SerializedName("part_no")
    @Expose
     var part_no: String? = null

    @SerializedName("product_no")
    @Expose
     var product_no: String? = null

    @SerializedName("gst")
    @Expose
     var gst: String? = null

    @SerializedName("discount")
    @Expose
     var discount: String? = null

    @SerializedName("amount_diff")
    @Expose
     var amount_diff: Double? = null

    @SerializedName("ebd_amount")
    @Expose
    var ebd_amount: String? = null

    @SerializedName("scheme_amount")
    @Expose
    var scheme_amount: String? = null

    @SerializedName("product_ebd_amount")
    @Expose
    var product_ebd_amount: String? = null

    @SerializedName("model_no")
    @Expose
    var model_no: String? = null
    @SerializedName("stage")
    @Expose
    var stage: String? = null

    @SerializedName("phase")
    @Expose
    var phase: String? = null
   @SerializedName("expiry_interval")
    @Expose
    var expiry_interval: String? = null
 @SerializedName("expiry_interval_preiod")
    @Expose
    var expiry_interval_preiod: String? = null

    var quantity: Int = 0
    var amount: String = ""
    var isAddToCart: Boolean = false

}
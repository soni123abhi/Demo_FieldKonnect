package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ProductModel {


    @SerializedName("status")
    @Expose
     val status: String? = null

    @SerializedName("message")
    @Expose
     val message: String? = null

    @SerializedName("data")
    @Expose
     lateinit var  data: ArrayList<Datum?>

    class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

         @SerializedName("product_detail_id")
        @Expose
        var productDetailId: Int? = null

        @SerializedName("product_name")
        @Expose
        var productName: String? = null

        @SerializedName("product_details_name")
        @Expose
        var productDetailsName: String? = null

       var quantity: Int = 0
        var isAddToCart: Boolean = false

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
        var subcategoryId: String? = null

        @SerializedName("subcategory_name")
        @Expose
        var subcategoryName: String? = null

        @SerializedName("category_id")
        @Expose
        var categoryId: String? = null

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

        @SerializedName("mrp")
        @Expose
        var mrp: Int? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("selling_price")
        @Expose
        var sellingPrice: String? = null

        @SerializedName("gst")
        @Expose
        var gst: String? = null
        var discountPercentage: String? = null
        var amountWithoutTax: String? = null
        var discountAmount: String? = null
        var amount: String? = null
        var tax: String? = null
        var isGST: Boolean? = null

    }
}
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ProductDetailModel {


    @SerializedName("status")
    @Expose
    val status: String? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: Data? = null

    class Data {
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

        @SerializedName("specification")
        @Expose
        val specification: String? = null

        @SerializedName("part_no")
        @Expose
        val part_no: String? = null

        @SerializedName("product_no")
        @Expose
        val product_no: String? = null


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
        var mrp: Float? = null

        @SerializedName("price")
        @Expose
        var price: Float? = null

        @SerializedName("selling_price")
        @Expose
        var sellingPrice: Float? = null

        @SerializedName("gst")
        @Expose
        var gst: Int? = null

        @SerializedName("details")
        @Expose
        var details: List<Detail>? = null


        class Detail {
            @SerializedName("detail_id")
            @Expose
            var detailId: Int? = null

            @SerializedName("detail_title")
            @Expose
            var detailTitle: String? = null

            @SerializedName("detail_description")
            @Expose
            var detailDescription: String? = null

            @SerializedName("mrp")
            @Expose
            var mrp: String? = null

            @SerializedName("price")
            @Expose
            var price: String? = null

            @SerializedName("selling_price")
            @Expose
            var sellingPrice: String? = null

            @SerializedName("gst")
            @Expose
            var gst: String? = null

            @SerializedName("discount")
            @Expose
            var discount: String? = null

            @SerializedName("max_discount")
            @Expose
            var max_discount: String? = null

            @SerializedName("min_price")
            @Expose
            var min_price: String? = null



        }
    }
}
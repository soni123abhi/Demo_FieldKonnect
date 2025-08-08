package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class InsertOrderRequestModel {

    @SerializedName("buyer_id")
    @Expose
    var buyer_id: String? = null

    @SerializedName("seller_id")
    @Expose
    var seller_id: String? = null


    @SerializedName("total_gst")
    @Expose
    var total_gst: String? = null


    @SerializedName("sub_total")
    @Expose
    var sub_total: String? = null


    @SerializedName("grand_total")
    @Expose
    var grand_total: String? = null

    @SerializedName("beatScheduleId")
    @Expose
    var beatScheduleId: String? = null

    @SerializedName("remark")
    @Expose
    var remark: String? = null

    @SerializedName("schme_amount")
    @Expose
    var schme_amount: String? = null

    @SerializedName("schme_val")
    @Expose
    var schme_val: String? = null

    @SerializedName("ebd_discount")
    @Expose
    var ebd_discount: String? = null

    @SerializedName("ebd_amount")
    @Expose
    var ebd_amount: String? = null

    @SerializedName("special_discount")
    @Expose
    var special_discount: String? = null

    @SerializedName("special_amount")
    @Expose
    var special_amount: String? = null

    @SerializedName("cluster_discount")
    @Expose
    var cluster_discount: String? = null

    @SerializedName("cluster_amount")
    @Expose
    var cluster_amount: String? = null

    @SerializedName("deal_discount")
    @Expose
    var deal_discount: String? = null

    @SerializedName("deal_amount")
    @Expose
    var deal_amount: String? = null

    @SerializedName("distributor_discount")
    @Expose
    var distributor_discount: String? = null

    @SerializedName("distributor_amount")
    @Expose
    var distributor_amount: String? = null

    @SerializedName("frieght_discount")
    @Expose
    var frieght_discount: String? = null

    @SerializedName("frieght_amount")
    @Expose
    var frieght_amount: String? = null

    @SerializedName("dod_discount")
    @Expose
    var dod_discount: String? = null

    @SerializedName("dod_discount_amount")
    @Expose
    var dod_discount_amount: String? = null

    @SerializedName("special_distribution_discount")
    @Expose
    var special_distribution_discount: String? = null

    @SerializedName("special_distribution_discount_amount")
    @Expose
    var special_distribution_discount_amount: String? = null

    @SerializedName("distribution_margin_discount")
    @Expose
    var distribution_margin_discount: String? = null

    @SerializedName("distribution_margin_discount_amount")
    @Expose
    var distribution_margin_discount_amount: String? = null

    @SerializedName("fan_extra_discount")
    @Expose
    var fan_extra_discount: String? = null

    @SerializedName("fan_extra_discount_amount")
    @Expose
    var fan_extra_discount_amount: String? = null

    @SerializedName("cash_discount")
    @Expose
    var cash_discount: String? = null

    @SerializedName("cash_amount")
    @Expose
    var cash_amount: String? = null

    @SerializedName("total_fan_discount")
    @Expose
    var total_fan_discount: String? = null

    @SerializedName("total_fan_discount_amount")
    @Expose
    var total_fan_discount_amount: String? = null

    @SerializedName("agri_standard_discount")
    @Expose
    var agri_standard_discount: String? = null

    @SerializedName("agri_standard_discount_amount")
    @Expose
    var agri_standard_discount_amount: String? = null

    @SerializedName("total_discount")
    @Expose
    var total_discount: String? = null

    @SerializedName("total_amount")
    @Expose
    var total_amount: String? = null

    @SerializedName("advance")
    @Expose
    var advance: String? = null

    @SerializedName("gst5_amt")
    @Expose
    var gst5_amt: String? = null

    @SerializedName("gst12_amt")
    @Expose
    var gst12_amt: String? = null

    @SerializedName("gst18_amt")
    @Expose
    var gst18_amt: String? = null

    @SerializedName("gst28_amt")
    @Expose
    var gst28_amt: String? = null

    @SerializedName("orderdetail")
    @Expose
    var orderdetail: List<Datum>? = null


    inner class Datum {

        @SerializedName("product_id")
        @Expose
        var product_id: String? = null

        @SerializedName("product_detail_id")
        @Expose
        var product_detail_id: String? = null

        @SerializedName("quantity")
        @Expose
        var quantity: String? = null

        @SerializedName("discount")
        @Expose
        var discount: String? = null

        @SerializedName("discount_amount")
        @Expose
        var discount_amount: String? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("line_total")
        @Expose
        var line_total: String? = null

        @SerializedName("ebd_amount")
        @Expose
        var ebd_amount: String? = null

        @SerializedName("tax_amount")
        @Expose
        var tax_amount: String? = null

    }
}
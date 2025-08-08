package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OrderDetailsModel {

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

        @SerializedName("buyer_id")
        @Expose
        var buyerId: String? = null

        @SerializedName("seller_id")
        @Expose
        var seller_id: String? = null

        @SerializedName("total_qty")
        @Expose
        var totalQty: String? = null

        @SerializedName("shipped_qty")
        @Expose
        var shippedQty: String? = null

        @SerializedName("orderno")
        @Expose
        var orderno: String? = null

        @SerializedName("order_date")
        @Expose
        var orderDate: String? = null

        @SerializedName("completed_date")
        @Expose
        var completedDate: Any? = null

        @SerializedName("total_gst")
        @Expose
        var totalGst: String? = null

        @SerializedName("sub_total")
        @Expose
        var subTotal: String? = null

        @SerializedName("grand_total")
        @Expose
        var grandTotal: String? = null

        @SerializedName("schme_amount")
        @Expose
        var schme_amount: String? = null

        @SerializedName("schme_val")
        @Expose
        var schme_val: String? = null

        @SerializedName("cluster_discount")
        @Expose
        var cluster_discount: String? = null

        @SerializedName("cluster_amount")
        @Expose
        var cluster_amount: String? = null

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

        @SerializedName("product_cat_id")
        @Expose
        var product_cat_id: Int? = null

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

        @SerializedName("agri_standard_discount")
        @Expose
        var agri_standard_discount: String? = null

        @SerializedName("agri_standard_discount_amount")
        @Expose
        var agri_standard_discount_amount: String? = null

        @SerializedName("total_discount")
        @Expose
        var total_discount: String? = null

        @SerializedName("advance")
        @Expose
        var advance: String? = null

        @SerializedName("total_amount")
        @Expose
        var total_amount: String? = null

        @SerializedName("total_fan_discount")
        @Expose
        var total_fan_discount: String? = null

        @SerializedName("total_fan_discount_amount")
        @Expose
        var total_fan_discount_amount: String? = null

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

        @SerializedName("order_remark")
        @Expose
        var order_remark: String? = null

        @SerializedName("seller_name")
        @Expose
        var seller_name: String? = null

        @SerializedName("buyer_name")
        @Expose
        var buyer_name: String? = null

        @SerializedName("buyer_type")
        @Expose
        var buyer_type: String? = null

        @SerializedName("created_by")
        @Expose
        var created_by: String? = null

        @SerializedName("order_status")
        @Expose
        var order_status: String? = null

        @SerializedName("status_id")
        @Expose
        var status_id: String? = null

        @SerializedName("dispatch_date")
        @Expose
        var dispatch_date: String? = null

        @SerializedName("lr_no")
        @Expose
        var lr_no: String? = null

        @SerializedName("invoice_no")
        @Expose
        var invoice_no: String? = null

        @SerializedName("invoice_date")
        @Expose
        var invoice_date: String? = null

        @SerializedName("transport_name")
        @Expose
        var transport_name: String? = null

        @SerializedName("orderdetails")
        @Expose
        var orderdetails: List<Orderdetail>? = null

        @SerializedName("sellers")
        @Expose
        var sellers : Sellers? = Sellers()

        @SerializedName("buyers")
        @Expose
        var buyers : Buyers? = Buyers()

        @SerializedName("buyer_address")
        @Expose
        var buyer_address : Buyer_address? = Buyer_address()

        @SerializedName("seller_address")
        @Expose
        var seller_address : Seller_address? = Seller_address()

        @SerializedName("createdbyname")
        @Expose
        var createdbyname : Createdbyname? = Createdbyname()



        class Orderdetail {
            @SerializedName("orderdetail_id")
            @Expose
            var orderdetailId: Int? = null

            @SerializedName("product_id")
            @Expose
            var productId: String? = null

            @SerializedName("product_name")
            @Expose
            var productName: String? = null

            @SerializedName("product_image")
            @Expose
            var productImage: String? = null

            @SerializedName("order_date")
            @Expose
            var order_date: String? = null

             @SerializedName("quantity")
            @Expose
            var quantity: String? = null

             @SerializedName("specification")
            @Expose
            var specification: String? = null

            @SerializedName("hp")
            @Expose
            var hp: String? = null

             @SerializedName("part_no")
            @Expose
            var part_no: String? = null

            @SerializedName("ebd_amount")
            @Expose
            var ebd_amount: String? = null

            @SerializedName("gst")
            @Expose
            var gst: String? = null

             @SerializedName("product_no")
            @Expose
            var product_no: String? = null

            @SerializedName("shipped_qty")
            @Expose
            var shippedQty: String? = null

            @SerializedName("price")
            @Expose
            var price: String? = null

            @SerializedName("model_no")
            @Expose
            var model_no: String? = null

            @SerializedName("phase")
            @Expose
            var phase: String? = null

            @SerializedName("brand_name")
            @Expose
            var brand_name: String? = null

            @SerializedName("tax_amount")
            @Expose
            var taxAmount: String? = null

            @SerializedName("line_total")
            @Expose
            var lineTotal: String? = null

            @SerializedName("status_id")
            @Expose
            var statusId: Int? = null

        }

        data class Buyers (
            @SerializedName("mobile")
            var mobile : String? = null,

            @SerializedName("customertypes")
            @Expose
            var customertypes : Customertypes? = Customertypes()
        )

        data class Sellers (
            @SerializedName("mobile")
            var mobile : String? = null,

        )
        data class Createdbyname (
            @SerializedName("name")
            var name : String? = null,

        )

        data class Buyer_address (
            @SerializedName("address1")
            var address1 : String? = null,


        )

        data class Seller_address (
            @SerializedName("address1")
            var address1 : String? = null,

        )
        data class Customertypes (

            @SerializedName("id")
            var id: Int? = null
        )

        /*class sellersdetails {
            @SerializedName("mobile")
            @Expose
            var mobile: String? = null
        }

        class buyersdetails {
            @SerializedName("mobile")
            @Expose
            var mobile: String? = null
        }*/
    }
}
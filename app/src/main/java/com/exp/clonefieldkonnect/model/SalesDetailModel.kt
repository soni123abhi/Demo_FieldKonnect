package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

 class SalesDetailModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()

      class Data {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("buyer_id")
          var buyerId: Int? = null

          @SerializedName("buyer_name")
          var buyerName: String? = null

          @SerializedName("seller_id")
          var sellerId: Int? = null

          @SerializedName("seller_name")
          var sellerName: String? = null

          @SerializedName("order_id")
          var orderId: Int? = null

          @SerializedName("total_gst")
          var totalGst: String? = null

          @SerializedName("sub_total")
          var subTotal: String? = null

          @SerializedName("grand_total")
          var grandTotal: String? = null

          @SerializedName("invoice_date")
          var invoiceDate: String? = null

          @SerializedName("invoice_no")
          var invoiceNo: String? = null

          @SerializedName("lr_no")
          var lr_no: String? = null

          @SerializedName("dispatch_date")
          var dispatch_date: String? = null

          @SerializedName("transport_name")
          var transport_name: String? = null

          @SerializedName("fiscal_year")
          var fiscalYear: String? = null

          @SerializedName("sales_no")
          var salesNo: String? = null

          @SerializedName("status_id")
          var statusId: Int? = null

          @SerializedName("saledetails")
          var saledetails: ArrayList<Saledetails> = arrayListOf()

      }


      class Saledetails {

          @SerializedName("sales_id")
          var salesId: Int? = null

          @SerializedName("product_id")
          var productId: Int? = null

          @SerializedName("quantity")
          var quantity: Int? = null

          @SerializedName("shipped_qty")
          var shippedQty: Int? = null

          @SerializedName("price")
          var price: String? = null

          @SerializedName("tax_amount")
          var taxAmount: String? = null

          @SerializedName("line_total")
          var lineTotal: String? = null

          @SerializedName("status_id")
          var statusId: String? = null

          @SerializedName("products")
          var products: Products? = Products()

      }

      class Products {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("product_name")
          var productName: String? = null

          @SerializedName("display_name")
          var displayName: String? = null

          @SerializedName("product_image")
          var productImage: String? = null

          @SerializedName("model_no")
          var model_no: String? = null

          @SerializedName("brands")
          var brands : Brands? = Brands()


      }
      class Brands {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("brand_name")
          var brandName: String? = null

          @SerializedName("brand_image")
          var brandImage: String? = null

      }

 }
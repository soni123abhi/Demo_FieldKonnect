package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class OrderPdfModel {

     @SerializedName("status")
     var status: String? = null
     @SerializedName("message")
     var message: String? = null
     @SerializedName("data")
     var data: Data? = Data()

     class Data {
         @SerializedName("pdf_url")
         var pdfUrl: String? = null

     }
 }

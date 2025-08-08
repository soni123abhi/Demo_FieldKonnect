package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class PrimarySchemeModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("data")
     var data: ArrayList<Data> = arrayListOf()

      class Data {

          @SerializedName("id")
          var id: Int? = null

          @SerializedName("scheme_name")
          var schemeName: String? = null

      }

 }
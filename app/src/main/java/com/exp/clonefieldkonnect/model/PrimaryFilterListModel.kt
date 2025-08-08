package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class PrimaryFilterListModel {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("data")
     var data: Data? = Data()


      class Data {

          @SerializedName("years")
          var years: ArrayList<Years> = arrayListOf()

          @SerializedName("divisions")
          var divisions: ArrayList<Divisions> = arrayListOf()

          @SerializedName("quarters")
          var quarters: ArrayList<Quarters> = arrayListOf()

          @SerializedName("types")
          var types: ArrayList<Types> = arrayListOf()

      }

      class Years {

          @SerializedName("key")
          var key: String? = null

          @SerializedName("value")
          var value: String? = null

      }


     class Types {

         @SerializedName("key")
         var key: String? = null

         @SerializedName("value")
         var value: String? = null

     }

     class Quarters {
         @SerializedName("key")
         var key: String? = null

         @SerializedName("value")
         var value: String? = null

     }

     class Divisions {

         @SerializedName("key")
         var key: String? = null

         @SerializedName("value")
         var value: String? = null

     }

 }
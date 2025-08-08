package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class EnquiryModel {


    @SerializedName("status")
    @Expose
    private val status: String? = null

    @SerializedName("message")
    @Expose
    private val message: String? = null

    @SerializedName("data")
    @Expose
     val data: ArrayList<Datum> = arrayListOf()


    class Datum {
        @SerializedName("field_id")
        @Expose
        var field_id: Int? = null

         @SerializedName("field_name")
        @Expose
        var fieldName: String? = null

        @SerializedName("field_type")
        @Expose
        var fieldType: String? = null

        @SerializedName("label_name")
        @Expose
        var labelName: String? = null

        @SerializedName("placeholder")
        @Expose
        var placeholder: String? = null

        @SerializedName("is_required")
        @Expose
        var isRequired: String? = null

        @SerializedName("is_multiple")
        @Expose
        var isMultiple: String? = null



         var value: String = ""
         var responseValue: String = ""

        @SerializedName("field_data")
        @Expose
        var fieldData: List<FieldDatum>? = null


        class FieldDatum {
            @SerializedName("value_id")
            @Expose
            var valueId: Int? = null

            @SerializedName("value")
            @Expose
            var value: String? = null

        }
    }
}
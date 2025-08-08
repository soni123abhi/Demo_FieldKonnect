package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

data class MspTabledataModel(

    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("activities" ) var activities : ArrayList<Activities> = arrayListOf(),
    @SerializedName("data") var data: Dataaaaa? = Dataaaaa(),
    @SerializedName("pdf_url") var pdfUrl : PdfUrl? = PdfUrl()

)
data class PdfUrl (

    @SerializedName("pdf_url" ) var pdfUrl : String? = null

)

data class Activities (

    @SerializedName("id"   ) var id   : Int?    = null,
    @SerializedName("type" ) var type : String? = null

)


data class Dataaaaa(

    @SerializedName("04/24") var april: ArrayList<april> = arrayListOf(),
    @SerializedName("05/24") var may: ArrayList<april> = arrayListOf(),
    @SerializedName("06/24") var june: ArrayList<april> = arrayListOf(),
    @SerializedName("07/24") var july: ArrayList<april> = arrayListOf(),
    @SerializedName("08/24") var augest: ArrayList<april> = arrayListOf(),
    @SerializedName("09/24") var sep: ArrayList<april> = arrayListOf(),
    @SerializedName("10/24") var oct: ArrayList<april> = arrayListOf(),
    @SerializedName("11/24") var nov: ArrayList<april> = arrayListOf(),
    @SerializedName("12/24") var dec: ArrayList<april> = arrayListOf(),
    @SerializedName("01/25") var jan: ArrayList<april> = arrayListOf(),
    @SerializedName("02/25") var feb: ArrayList<april> = arrayListOf(),
    @SerializedName("03/25") var mar: ArrayList<april> = arrayListOf()

)

data class april(

    @SerializedName("activity_name") var activityName: String? = null,
    @SerializedName("total_performed") var totalPerformed: Int? = null,
    @SerializedName("total_participants") var totalParticipants: Int? = null

)
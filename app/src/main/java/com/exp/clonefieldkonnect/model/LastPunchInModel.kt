package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class LastPunchInModel {


    @SerializedName("punchin_id")
    @Expose
    private var punchinId: Int? = null

    @SerializedName("punchin_date")
    @Expose
    private var punchinDate: String? = null

    @SerializedName("punchin_time")
    @Expose
    private var punchinTime: String? = null

    @SerializedName("punchin_longitude")
    @Expose
    private var punchinLongitude: String? = null

    @SerializedName("punchin_latitude")
    @Expose
    private var punchinLatitude: String? = null

    @SerializedName("punchin_address")
    @Expose
    private var punchinAddress: String? = null

    @SerializedName("punchin_image")
    @Expose
    private var punchinImage: String? = null

    @SerializedName("punchout_date")
    @Expose
    private var punchoutDate: String? = null

    @SerializedName("punchout_time")
    @Expose
    private var punchoutTime: String? = null

    @SerializedName("punchout_latitude")
    @Expose
    private var punchoutLatitude: String? = null

    @SerializedName("punchout_longitude")
    @Expose
    private var punchoutLongitude: String? = null

    @SerializedName("punchout_address")
    @Expose
    private var punchoutAddress: String? = null

    @SerializedName("working_type")
    @Expose
    private var working_type: String? = null

    @SerializedName("punchout_image")
    @Expose
    private var punchoutImage: String? = null

    fun getPunchinId(): Int? {
        return punchinId
    }

    fun setPunchinId(punchinId: Int?) {
        this.punchinId = punchinId
    }

    fun getWrokingType(): String? {
        return working_type
    }

    fun setWrokingType(working_type: String?) {
        this.working_type = working_type
    }

    fun getPunchinDate(): String? {
        return punchinDate
    }

    fun setPunchinDate(punchinDate: String?) {
        this.punchinDate = punchinDate
    }

    fun getPunchinTime(): String? {
        return punchinTime
    }

    fun setPunchinTime(punchinTime: String?) {
        this.punchinTime = punchinTime
    }

    fun getPunchinLongitude(): String? {
        return punchinLongitude
    }

    fun setPunchinLongitude(punchinLongitude: String?) {
        this.punchinLongitude = punchinLongitude
    }

    fun getPunchinLatitude(): String? {
        return punchinLatitude
    }

    fun setPunchinLatitude(punchinLatitude: String?) {
        this.punchinLatitude = punchinLatitude
    }

    fun getPunchinAddress(): String? {
        return punchinAddress
    }

    fun setPunchinAddress(punchinAddress: String?) {
        this.punchinAddress = punchinAddress
    }

    fun getPunchinImage(): String? {
        return punchinImage
    }

    fun setPunchinImage(punchinImage: String?) {
        this.punchinImage = punchinImage
    }

    fun getPunchoutDate(): String? {
        return punchoutDate
    }

    fun setPunchoutDate(punchoutDate: String?) {
        this.punchoutDate = punchoutDate
    }

    fun getPunchoutTime(): String? {
        return punchoutTime
    }

    fun setPunchoutTime(punchoutTime: String?) {
        this.punchoutTime = punchoutTime
    }

    fun getPunchoutLatitude(): String? {
        return punchoutLatitude
    }

    fun setPunchoutLatitude(punchoutLatitude: String?) {
        this.punchoutLatitude = punchoutLatitude
    }

    fun getPunchoutLongitude(): String? {
        return punchoutLongitude
    }

    fun setPunchoutLongitude(punchoutLongitude: String?) {
        this.punchoutLongitude = punchoutLongitude
    }

    fun getPunchoutAddress(): String? {
        return punchoutAddress
    }

    fun setPunchoutAddress(punchoutAddress: String?) {
        this.punchoutAddress = punchoutAddress
    }

    fun getPunchoutImage(): String? {
        return punchoutImage
    }

    fun setPunchoutImage(punchoutImage: String?) {
        this.punchoutImage = punchoutImage
    }
}
package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName


class AttendanceDetailModel {

    @SerializedName("status")
    var status: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("data")
    var data: Data? = Data()


    class Data {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("active")
        var active: String? = null

        @SerializedName("user_id")
        var userId: String? = null

        @SerializedName("punchin_date")
        var punchinDate: String? = null

        @SerializedName("punchin_time")
        var punchinTime: String? = null

        @SerializedName("punchin_longitude")
        var punchinLongitude: String? = null

        @SerializedName("punchin_latitude")
        var punchinLatitude: String? = null

        @SerializedName("punchin_address")
        var punchinAddress: String? = null

        @SerializedName("punchin_image")
        var punchinImage: String? = null

        @SerializedName("punchout_date")
        var punchoutDate: String? = null

        @SerializedName("punchout_time")
        var punchoutTime: String? = null

        @SerializedName("punchout_latitude")
        var punchoutLatitude: String? = null

        @SerializedName("punchout_longitude")
        var punchoutLongitude: String? = null

        @SerializedName("punchout_address")
        var punchoutAddress: String? = null

        @SerializedName("punchout_image")
        var punchoutImage: String? = null

        @SerializedName("punchin_summary")
        var punchinSummary: String? = null

        @SerializedName("punchout_summary")
        var punchoutSummary: String? = null

        @SerializedName("worked_time")
        var workedTime: String? = null

        @SerializedName("attendance_status")
        var attendanceStatus: String? = null

        @SerializedName("remark_status")
        var remarkStatus: String? = null

        @SerializedName("deleted_at")
        var deletedAt: String? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("updated_at")
        var updatedAt: String? = null

        @SerializedName("working_type")
        var workingType: String? = null

        @SerializedName("users")
        var users: Users? = Users()


        class Users {

            @SerializedName("id")
            var id: Int? = null

            @SerializedName("name")
            var name: String? = null

            @SerializedName("email")
            var email: String? = null

            @SerializedName("email_verified_at")
            var emailVerifiedAt: String? = null

            @SerializedName("created_at")
            var createdAt: String? = null

            @SerializedName("updated_at")
            var updatedAt: String? = null

            @SerializedName("active")
            var active: String? = null

            @SerializedName("first_name")
            var firstName: String? = null

            @SerializedName("last_name")
            var lastName: String? = null

            @SerializedName("mobile")
            var mobile: String? = null

            @SerializedName("notification_id")
            var notificationId: String? = null

            @SerializedName("device_type")
            var deviceType: String? = null

            @SerializedName("gender")
            var gender: String? = null

            @SerializedName("profile_image")
            var profileImage: String? = null

            @SerializedName("latitude")
            var latitude: String? = null

            @SerializedName("longitude")
            var longitude: String? = null

            @SerializedName("user_code")
            var userCode: String? = null

            @SerializedName("location")
            var location: String? = null

            @SerializedName("reportingid")
            var reportingid: String? = null

            @SerializedName("region_id")
            var regionId: String? = null

            @SerializedName("employee_codes")
            var employeeCodes: String? = null

            @SerializedName("branch_id")
            var branchId: String? = null

            @SerializedName("designation_id")
            var designationId: String? = null

            @SerializedName("department_id")
            var departmentId: String? = null

            @SerializedName("created_by")
            var createdBy: String? = null

            @SerializedName("deleted_at")
            var deletedAt: String? = null

        }
    }
}

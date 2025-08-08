package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class UserActivityListModel {

    @SerializedName("status")
    var status: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("users")
    var users: ArrayList<Users> = arrayListOf()
    @SerializedName("branches")
    var branches: ArrayList<Branches> = arrayListOf()
    @SerializedName("page_count")
    var pageCount: Int? = null
    @SerializedName("data")
    var data: ArrayList<Data> = arrayListOf()


    class Users {

        @SerializedName("id")
        var id: Int? = null

        @SerializedName("name")
        var name: String? = null
    }


        class Branches {

            @SerializedName("id")
            var id: Int? = null

            @SerializedName("name")
            var name: String? = null
        }


    class Data {

                @SerializedName("user_id")
                var userId: Int? = null

                @SerializedName("name")
                var name: String? = null

                @SerializedName("date")
                var date: String? = null

    }
}

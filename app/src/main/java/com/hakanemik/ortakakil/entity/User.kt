package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("surname")
    var surname: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("nameSurname")
    var nameSurname:String,
    @SerializedName("isActive")
    var isActive: Boolean,
    @SerializedName("createdDate")
    var createdDate: String,
    @SerializedName("totalDecisionCount")
    var totalDecisionCount: Int,
){}

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
    var nameSurname:String? = null,
    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("createdDate")
    var createdDate: String? = null,
    @SerializedName("totalDecisionCount")
    var totalDecisionCount: Int? = null,
    @SerializedName("authProvider")
    var authProvider: String? = null

)

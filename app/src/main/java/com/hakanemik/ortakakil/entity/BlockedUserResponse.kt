package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class BlockedUserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("pictureUrl")
    val pictureUrl: String?,
    @SerializedName("createdDate")
    val createdDate: String
)

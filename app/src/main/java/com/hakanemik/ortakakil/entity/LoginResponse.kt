package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    var accessToken: String,
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("tokenExpiry")
    var tokenExpiry: String,
    @SerializedName("refreshExpiry")
    var refreshExpiry: String,
    @SerializedName("user")
    var user: User
)

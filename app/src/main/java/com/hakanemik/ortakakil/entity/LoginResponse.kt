package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    @Expose
    var accessToken: String,
    @SerializedName("refreshToken")
    @Expose
    var refreshToken: String,
    @SerializedName("tokenExpiry")
    @Expose
    var tokenExpiry: String,
    @SerializedName("refreshExpiry")
    @Expose
    var refreshExpiry: String,
    @SerializedName("user")
    @Expose
    var user: User
)

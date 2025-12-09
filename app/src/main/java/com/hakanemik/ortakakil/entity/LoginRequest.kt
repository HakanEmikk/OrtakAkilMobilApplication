package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
){}

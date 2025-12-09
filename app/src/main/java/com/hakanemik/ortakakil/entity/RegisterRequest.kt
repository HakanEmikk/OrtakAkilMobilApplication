package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("surname")
    var surname: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("confirmPassword")
    var confirmPassword: String
)

package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("surname")
    @Expose
    var surname: String,
    @SerializedName("email")
    @Expose
    var email: String,
    @SerializedName("password")
    @Expose
    var password: String,
    @SerializedName("confirmPassword")
    @Expose
    var confirmPassword: String
) {
}
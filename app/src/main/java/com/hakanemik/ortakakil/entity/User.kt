package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("surname")
    @Expose
    var surname: String,
    @SerializedName("email")
    @Expose
    var email: String,
    @SerializedName("nameSurname")
    @Expose
    var nameSurname:String,
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean,
    @SerializedName("createdDate")
    @Expose
    var createdDate: String
){}

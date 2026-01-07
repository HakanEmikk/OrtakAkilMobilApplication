package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("id")
    val id:Int,
    @SerializedName("content")
    val content:String,
    @SerializedName("userFullName")
    val userFullName:String,
    @SerializedName("userProfilePicture")
    val userProfilePicture:String,
    @SerializedName("createdDate")
    val createdDate:String
)
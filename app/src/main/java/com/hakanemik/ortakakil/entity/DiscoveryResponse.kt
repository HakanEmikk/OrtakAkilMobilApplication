package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class DiscoveryResponse(
    @SerializedName("decisionId")
    val decisionId:Int,
    @SerializedName("userId")
    val userId:Int,
    @SerializedName("answer")
    val answer:String,
    @SerializedName("title")
    val title:String,
    @SerializedName("shareNote")
    val shareNote:String,
    @SerializedName("category")
    val category:String,
    @SerializedName("userFullName")
    val userFullName:String,
    @SerializedName("userPhotoUrl")
    val userPhotoUrl:String? = null,
    @SerializedName("likeCount")
    val likeCount:Int,
    @SerializedName("commentCount")
    val commentCount:Int,
    @SerializedName("isLikedByMe")
    val isLikedByMe:Boolean,
    @SerializedName("createdDate")
    val createdDate:String
)

package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("decisionId")
    val decisionId:Int,
    @SerializedName("answer")
    val answer:String,
    @SerializedName("title")
    val title:String,
    @SerializedName("shareNote")
    val shareNote:String,
    @SerializedName("category")
    val category:String,
    @SerializedName("likeCount")
    val likeCount:Int,
    @SerializedName("commentCount")
    val commentCount:Int,
    @SerializedName("isPublic")
    val isPublic:Boolean,
    @SerializedName("createdDate")
    val createdDate:String
)

package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("decisionId")
    val decisionId:Int,
    @SerializedName("content")
    val content:String
)

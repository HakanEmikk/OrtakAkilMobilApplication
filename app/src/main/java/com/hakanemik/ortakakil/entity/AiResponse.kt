package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AiResponse(
    @SerializedName("decisionId")
    val decisionId: Int,
    @SerializedName("answer")
    val answer: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("createdAt")
    val createdAt: String
)

package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AiResponse(
    @SerializedName("decisionId")
    @Expose
    val decisionId: Int,
    @SerializedName("answer")
    @Expose
    val answer: String,
    @SerializedName("category")
    @Expose
    val category: String,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String
)

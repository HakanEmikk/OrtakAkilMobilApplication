package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AiRequest(
    @SerializedName("question")
    @Expose
    val question: String,
    @SerializedName("category")
    @Expose
    val category: String
)

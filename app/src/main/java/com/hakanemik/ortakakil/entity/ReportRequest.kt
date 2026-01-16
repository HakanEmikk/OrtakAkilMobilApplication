package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("decisionId")
    val decisionId: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("reason")
    val reason: String
)

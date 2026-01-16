package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.SerializedName

data class ShareRequest(
    @SerializedName("decisionId")
    val decisionId: Int,
    @SerializedName("note")
    val note: String?,
)

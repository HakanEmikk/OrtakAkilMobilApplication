package com.hakanemik.ortakakil.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AiApiResponse(
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("message")
    @Expose
    val message: String?,
    @SerializedName("data")
    @Expose
    val data: AiResponse?,
    @SerializedName("error")
    @Expose
    val error: List<String?>
)

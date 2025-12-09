package com.hakanemik.ortakakil.entity

data class ProfileUiState(
    val userName: String = "Misafir",
    val imageUrl: String = "",
    val email: String = "",
    val totalDecisionCount:String = "",
    val totalShareCount:String = "0",
    val isLoading: Boolean = false,
    val error: String? = null
)

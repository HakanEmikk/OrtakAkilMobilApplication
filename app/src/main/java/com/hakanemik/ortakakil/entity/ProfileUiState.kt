package com.hakanemik.ortakakil.entity

data class ProfileUiState(
    val userName: String = "Misafir",
    val email: String = "",
    val totalDecisionCount:String = "",
    val totalShareCount:String = "0",
    val photoUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val deleteAccountState: Resource<Boolean>? = null
)

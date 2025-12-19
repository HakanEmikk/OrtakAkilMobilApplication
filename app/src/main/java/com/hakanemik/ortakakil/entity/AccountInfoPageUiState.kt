package com.hakanemik.ortakakil.entity

data class AccountInfoPageUiState(
    val id: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val authProvider: String = "",
    val error: String? = null
)


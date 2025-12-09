package com.hakanemik.ortakakil.entity

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginState: Resource<ApiResponse<LoginResponse>>? = null,
    val isAutoLogging: Boolean = false
)
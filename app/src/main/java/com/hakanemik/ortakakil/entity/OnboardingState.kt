package com.hakanemik.ortakakil.entity

data class OnboardingState(
    val loginState: Resource<ApiResponse<LoginResponse>>? = null,
)

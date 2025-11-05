package com.hakanemik.ortakakil.entity

data class HomeUiState(
    val userName: String = "Misafir",
    val userId: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val question: String = "",
    val selected: String = "",
    val isClicked: Boolean = false
)
package com.hakanemik.ortakakil.entity

data class HistoryUiState(
    val list: List<HistoryResponse> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val shareNote: String = ""
)

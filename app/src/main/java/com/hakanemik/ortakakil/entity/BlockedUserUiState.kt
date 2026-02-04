package com.hakanemik.ortakakil.entity

data class BlockedUsersUiState(
    val isLoading: Boolean = false,
    val blockedUsers: List<BlockedUserResponse> = emptyList(),
    val error: String? = null,
    val unblockSuccess: String? = null
)
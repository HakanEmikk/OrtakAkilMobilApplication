package com.hakanemik.ortakakil.entity

import android.net.Uri

data class AccountInfoPageUiState(
    val id: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val authProvider: String = "",
    val error: String? = null,
    val photoUrl: String? = null,
    val photoUri: Uri? = null
)


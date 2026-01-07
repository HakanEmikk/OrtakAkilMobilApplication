package com.hakanemik.ortakakil.entity

data class DiscoveryUiState(
    val list:List<DiscoveryResponse> = listOf(),
    val isLoading:Boolean = false,
    val error:String? = null,
    val selectedComments:List<CommentResponse> = listOf()
)
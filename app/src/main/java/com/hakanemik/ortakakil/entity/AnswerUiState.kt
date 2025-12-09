package com.hakanemik.ortakakil.entity

data class AnswerUiState(
    val answer:String = "",
    val question:String = "Soru yüklenıyor...",
    val category:String = "Genel",
    val isLoading:Boolean = false
)

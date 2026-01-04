package com.hakanemik.ortakakil.entity

data class AnswerUiState(
    val id:Int = 0,
    val answer:String = "",
    val question:String = "Soru yüklenıyor...",
    val category:String = "Genel",
    val isLoading:Boolean = false,
    val shareNote:String= ""
)

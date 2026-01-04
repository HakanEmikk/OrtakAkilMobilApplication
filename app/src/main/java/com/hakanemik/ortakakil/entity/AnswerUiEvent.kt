package com.hakanemik.ortakakil.entity

sealed interface AnswerUiEvent {
    data object ShareSuccess : AnswerUiEvent
    data object ShareError : AnswerUiEvent
}
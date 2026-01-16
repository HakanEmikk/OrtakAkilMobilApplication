package com.hakanemik.ortakakil.entity

sealed interface AnswerUiEvent {
    data object ShareSuccess : AnswerUiEvent
    data object ShareError : AnswerUiEvent
    data object UnshareSuccess : AnswerUiEvent
    data object UnshareError : AnswerUiEvent
    data object ReportSuccess : AnswerUiEvent
    data object ReportError : AnswerUiEvent
    data object BlockSuccess : AnswerUiEvent
    data object BlockError : AnswerUiEvent
}
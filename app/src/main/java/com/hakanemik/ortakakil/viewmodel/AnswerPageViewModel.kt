package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.AiRequest
import com.hakanemik.ortakakil.entity.AnswerUiEvent
import com.hakanemik.ortakakil.entity.AnswerUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.entity.ShareRequest
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnswerPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AnswerUiState())
    val uiState: StateFlow<AnswerUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AnswerUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadAnswer(question: String, category: String) {
        // İlk yükleme durumunu ayarla
        _uiState.update { it.copy(isLoading = true, question = question, category = category) }

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.aiRequest(AiRequest(question = question, category = category))

            when (response) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            id = response.data.data?.decisionId ?: 0,
                            answer = response.data.data?.answer ?: "Cevap bulunamadı",
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            answer = "Bir hata oluştu. Lütfen tekrar deneyiniz",
                            isLoading = false
                        )
                    }
                }

                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun onValueChange(value: String) {
        _uiState.update { it.copy(shareNote = value) }
    }

    fun onClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val shareRequest = ShareRequest(_uiState.value.id, _uiState.value.shareNote)
            val response = repository.shareAnswer(shareRequest)
            when (response) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isShared = true) }
                    _uiEvent.send(AnswerUiEvent.ShareSuccess)
                }

                is Resource.Error -> {
                    _uiEvent.send(AnswerUiEvent.ShareError)
                }

                else -> {}
            }
        }
    }
}

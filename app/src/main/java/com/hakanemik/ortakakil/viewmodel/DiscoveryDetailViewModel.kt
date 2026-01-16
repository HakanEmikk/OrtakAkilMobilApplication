package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.hakanemik.ortakakil.entity.DiscoveryUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.entity.CommentRequest
import com.hakanemik.ortakakil.entity.AnswerUiEvent
import com.hakanemik.ortakakil.entity.ReportRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class DiscoveryDetailViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AnswerUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun toggleLike(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.likeDecision(id)
        }
    }

    fun getComments(decisionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getComments(decisionId)) {
                is Resource.Success -> {
                    val comments = result.data.data ?: emptyList()
                    _uiState.update { it.copy(selectedComments = comments) }
                }

                is Resource.Error -> {
                    // Hata yönetimi
                }

                is Resource.Loading -> {
                    // Loading yönetimi
                }
            }
        }
    }

    fun addComment(content: String, decisionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = CommentRequest(decisionId, content)
            when (val result = repository.addComment(request)) {
                is Resource.Success -> {
                    getComments(decisionId)
                }
                is Resource.Error -> {
                    // Hata yönetimi
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun reportContent(decisionId: Int, reason: String) {
        viewModelScope.launch {
            val request = ReportRequest(decisionId, "Inappropriate content: $reason", reason)
            when (repository.reportAnswer(request)) {
                is Resource.Success -> _uiEvent.send(AnswerUiEvent.ReportSuccess)
                is Resource.Error -> _uiEvent.send(AnswerUiEvent.ReportError)
                else -> {}
            }
        }
    }

    fun blockUser(userId: Int) {
        viewModelScope.launch {

            when (repository.blockUser(userId )) {
                is Resource.Success -> _uiEvent.send(AnswerUiEvent.BlockSuccess)
                is Resource.Error -> _uiEvent.send(AnswerUiEvent.BlockError)
                else -> {}
            }
        }
    }
}
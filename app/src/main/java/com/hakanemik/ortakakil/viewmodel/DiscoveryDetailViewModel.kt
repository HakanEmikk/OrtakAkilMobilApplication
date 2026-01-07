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
import javax.inject.Inject

@HiltViewModel
class DiscoveryDetailViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

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
}
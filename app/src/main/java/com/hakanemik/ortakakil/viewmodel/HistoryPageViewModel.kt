package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.HistoryResponse
import com.hakanemik.ortakakil.entity.HistoryUiState
import com.hakanemik.ortakakil.entity.CommentResponse
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    
    private val _selectedComments = MutableStateFlow<List<CommentResponse>>(emptyList())
    val selectedComments: StateFlow<List<CommentResponse>> = _selectedComments.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoadingMore = false

    init {
        loadHistory(isInitial = true)
    }

    fun loadHistory(isInitial: Boolean = false) {
        if (isLoadingMore || (isLastPage && !isInitial)) return
        isLoadingMore = true

        if (isInitial) {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }
            currentPage = 1
            isLastPage = false
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getHistory(currentPage)) {
                is Resource.Success -> {
                    val newItems = result.data.data ?: emptyList()
                    val currentList = if (isInitial) emptyList() else _uiState.value.list

                    if (newItems.isEmpty()) {
                        isLastPage = true
                    } else {
                        currentPage++
                    }

                    _uiState.update {
                        it.copy(
                            list = (currentList + newItems),
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
            isLoadingMore = false
        }
    }

    fun loadMore() {
        if (!isLoadingMore && !isLastPage) {
            loadHistory(isInitial = false)
        }
    }

    fun refreshHistory() {
        loadHistory(isInitial = true)
    }

    fun getComments(decisionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getComments(decisionId)) {
                is Resource.Success -> {
                    _selectedComments.value = result.data.data ?: emptyList()
                }
                is Resource.Error -> {
                    // Handle error if needed
                }
                is Resource.Loading -> {
                    // Handle loading if needed
                }
            }
        }
    }
}
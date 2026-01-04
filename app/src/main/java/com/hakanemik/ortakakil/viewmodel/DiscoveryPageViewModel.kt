package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.DiscoveryResponse
import com.hakanemik.ortakakil.entity.DiscoveryUiState
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
class DiscoveryPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
):ViewModel() {
    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState : StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoadingMore = false

    init {
        loadFeed(isInitial = true)
    }

    private fun loadFeed(isInitial: Boolean = false) {
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
            when (val result = repository.loadFeed(currentPage)) {
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
            loadFeed(isInitial = false)
        }
    }

    fun refreshFeed() {
        loadFeed(isInitial = true)
    }

    fun toggleLike(id: Int) {
        val currentList = _uiState.value.list.toMutableList()
        val index = currentList.indexOfFirst { it.decisionId == id }

        if (index != -1) {
            val item = currentList[index]
            val newIsLiked = !item.isLikedByMe
            val newLikeCount = if (newIsLiked) item.likeCount + 1 else item.likeCount - 1
            
            // Optimistic Update
            currentList[index] = item.copy(isLikedByMe = newIsLiked, likeCount = newLikeCount)
            _uiState.update { it.copy(list = currentList) }

            viewModelScope.launch(Dispatchers.IO) {
                val response = repository.likeDecision(id)
                /*
                if (response is Resource.Error) {
                    // Revert if error
                    val revertedList = _uiState.value.list.toMutableList()
                    val revertIndex = revertedList.indexOfFirst { it.decisionId == id }
                    if (revertIndex != -1) {
                        revertedList[revertIndex] = item // Revert to original item
                        _uiState.update { it.copy(list = revertedList, error = "Beğeni işlemi başarısız") }
                    }
                }
                */
            }
        }
    }
}
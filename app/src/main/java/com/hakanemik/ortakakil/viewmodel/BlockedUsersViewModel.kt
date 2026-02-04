package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.BlockedUsersUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class BlockedUsersViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlockedUsersUiState())
    val uiState: StateFlow<BlockedUsersUiState> = _uiState.asStateFlow()

    init {
        getBlockedUsers()
    }

    private fun getBlockedUsers() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            when (val result = repository.getBlockedUsers()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            blockedUsers = result.data.data ?: emptyList()
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
                else -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    fun unblockUser(blockedId: Int) {
        viewModelScope.launch {
            when (val result = repository.unblockUser(blockedId)) {
                is Resource.Success -> {
                     // remove locally
                    val currentList = _uiState.value.blockedUsers.toMutableList()
                    currentList.removeAll { it.id == blockedId }
                    _uiState.update {
                        it.copy(
                            blockedUsers = currentList,
                            unblockSuccess = "Engel kaldırıldı"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Hata oluştu"
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun clearMessage() {
        _uiState.update {
            it.copy(
                error = null,
                unblockSuccess = null
            )
        }
    }
}

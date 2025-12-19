package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.ProfileUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import com.hakanemik.ortakakil.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private  val _uiState = MutableStateFlow(ProfileUiState())
    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile(){

        viewModelScope.launch {
            val response=repository.loadProfile()
            when(response){
                is Resource.Success ->{
                    _uiState.value = _uiState.value.copy(
                        totalDecisionCount = response.data.data?.totalDecisionCount.toString(),
                        isLoading = false
                    )
                    userRepository.getUserNameFlow().collectLatest { name ->
                        _uiState.value = _uiState.value.copy(
                            userName = name ?: "Misafir",
                            error = null
                        )
                    }
                    userRepository.getUserEmailFlow().collectLatest {
                        _uiState.value = _uiState.value.copy(
                            email = it ?: "Misafir",
                            error = null
                        )
                    }
                }
                is Resource.Error ->{
                    _uiState.value = _uiState.value.copy(
                        error = response.message ?: "Bir hata oluştu",
                        isLoading = false
                    )
                }
                else -> {

                }
            }
        }
    }
}
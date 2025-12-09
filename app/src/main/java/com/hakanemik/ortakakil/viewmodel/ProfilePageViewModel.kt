package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.ProfileUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
): ViewModel() {

    private  val _uiState = MutableStateFlow(ProfileUiState())
    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile(){

        viewModelScope.launch {
            val response=repository.loadProfile()
            when(response){
                is Resource.Success ->{
                    _uiState.value = _uiState.value.copy(
                        userName = response.data.data?.name ?: "",
                        email = response.data.data?.email ?: "",
                        totalDecisionCount = response.data.data?.totalDecisionCount.toString(),
                        isLoading = false
                    )
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
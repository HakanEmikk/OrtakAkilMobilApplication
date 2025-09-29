package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.RegisterApiResponse
import com.hakanemik.ortakakil.entity.RegisterRequest
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import kotlinx.coroutines.launch

class RegisterPageViewModel :ViewModel() {
    var OrtakAkilDaoRepository = OrtakAkilDaoRepository()
    private val _uiState = MutableLiveData<Resource<RegisterApiResponse>>()
    val uiState: LiveData<Resource<RegisterApiResponse>> = _uiState
    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            _uiState.value = OrtakAkilDaoRepository.register(registerRequest)

        }
    }
}
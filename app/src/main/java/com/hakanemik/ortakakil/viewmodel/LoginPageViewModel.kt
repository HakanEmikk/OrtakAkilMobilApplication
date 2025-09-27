package com.hakanemik.ortakakil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.LoginApiResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginPageViewModel :ViewModel() {
    var OrtakAkilDaoRepository = OrtakAkilDaoRepository()
    private val _uiState = MutableLiveData<Resource<LoginApiResponse>>()
    val uiState: LiveData<Resource<LoginApiResponse>> = _uiState
    fun login(loginRequest: LoginRequest){
        viewModelScope.
        launch{
            _uiState.value = Resource.Loading
            _uiState.value = OrtakAkilDaoRepository.login(loginRequest)

        }


    }

}
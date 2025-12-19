package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.AccountInfoPageUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.entity.User
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import com.hakanemik.ortakakil.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInfoPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository,
    private val userRepository: UserRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(AccountInfoPageUiState())
    val uiState: StateFlow<AccountInfoPageUiState> = _uiState.asStateFlow()

    init {
        loadAccount()
    }
    fun updateFirstName(it: String) {
        _uiState.value = _uiState.value.copy(firstName = it)
    }

    fun updateLastName(it: String) {
        _uiState.value = _uiState.value.copy(lastName = it)
    }

    fun updateEmail(it: String) {
        _uiState.value = _uiState.value.copy(email = it)
    }
    fun updateProfile(){
        viewModelScope.launch {
            when(val response=repository
                .updateProfile(
                    User(
                        id = _uiState.value.id ?: 0 ,
                        name = _uiState.value.firstName,
                        surname = _uiState.value.lastName,
                        email = _uiState.value.email,
                    )
                )
            ){
                is Resource.Success ->{
                    _uiState.value = _uiState.value.copy(
                        id = response.data.data?.id,
                        firstName = response.data.data?.name ?:"",
                        lastName = response.data.data?.surname ?:"",
                        email = response.data.data?.email ?:"",
                    )
                    userRepository.saveUserInfo(
                        userId = response.data.data?.id.toString(),
                        userName = response.data.data?.name ?:"",
                        email = response.data.data?.email ?:""
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = response.message ?: "Bir hata oluştu",
                    )
                }
                else->{}
            }
        }
    }

    private fun loadAccount() {
        viewModelScope.launch {
            when (val response = repository.loadProfile()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        id = response.data.data?.id ?: 0,
                        firstName = response.data.data?.name ?: "",
                        lastName = response.data.data?.surname ?: "",
                        email = response.data.data?.email ?: "",
                        authProvider = response.data.data?.authProvider ?: ""
                        )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = response.message ?: "Bir hata oluştu",
                    )
                }

                else -> {

                }

            }
        }

    }
}
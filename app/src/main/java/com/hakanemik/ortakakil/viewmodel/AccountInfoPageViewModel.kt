package com.hakanemik.ortakakil.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.AccountInfoPageUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.entity.User
import com.hakanemik.ortakakil.repo.FirebaseStorageRepository
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import com.hakanemik.ortakakil.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInfoPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository,
    private val userRepository: UserRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AccountInfoPageUiState())
    val uiState: StateFlow<AccountInfoPageUiState> = _uiState.asStateFlow()

    init {
        loadAccount()
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(photoUri = uri) }
    }

    fun updateFirstName(it: String) {
        _uiState.update { state -> state.copy(firstName = it) }
    }

    fun updateLastName(it: String) {
        _uiState.update { state -> state.copy(lastName = it) }
    }

    fun updateEmail(it: String) {
        _uiState.update { state -> state.copy(email = it) }
    }

    fun updateProfile() {

        viewModelScope.launch {
            try {
                // Mevcut state'deki değerleri güvenli bir şekilde alıyoruz
                val currentState = _uiState.value
                var finalPhotoUrl = currentState.photoUrl

                currentState.photoUri?.let { uri ->
                    finalPhotoUrl = firebaseStorageRepository.uploadProfilePhotoAndGetUrl(
                        userId = currentState.id.toString(),
                        photoUri = uri
                    )
                }

                val userToUpdate = User(
                    id = currentState.id ?: 0,
                    name = currentState.firstName,
                    surname = currentState.lastName,
                    email = currentState.email,
                    pictureUrl = finalPhotoUrl
                )

                when (val response = repository.updateProfile(userToUpdate)) {
                    is Resource.Success -> {
                        val data = response.data.data
                        _uiState.update { state ->
                            state.copy(
                                id = data?.id,
                                firstName = data?.name ?: "",
                                lastName = data?.surname ?: "",
                                email = data?.email ?: "",
                                photoUrl = data?.pictureUrl ?: "",
                                photoUri = null
                            )
                        }
                        userRepository.saveUserInfo(
                            userId = data?.id.toString(),
                            userName = data?.name ?: "",
                            email = data?.email ?: "",
                            pictureUrl = data?.pictureUrl ?: ""
                        )
                    }
                    is Resource.Error -> {
                        _uiState.update { state ->
                            state.copy(error = response.message ?: "Bir hata oluştu")
                        }
                    }
                    Resource.Loading -> { /* Gerekirse loading eklenebilir */ }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Bir hata oluştu") }
            }
        }
    }

    private fun loadAccount() {
        viewModelScope.launch {
            when (val response = repository.loadProfile()) {
                is Resource.Success -> {
                    val data = response.data.data
                    _uiState.update { state ->
                        state.copy(
                            id = data?.id ?: 0,
                            firstName = data?.name ?: "",
                            lastName = data?.surname ?: "",
                            email = data?.email ?: "",
                            authProvider = data?.authProvider ?: "",
                            photoUrl = data?.pictureUrl ?: ""
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { state ->
                        state.copy(error = response.message ?: "Bir hata oluştu")
                    }
                }
                else -> { }
            }
        }
    }
}
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            // 1. Önce backend'den profil verilerini (count vb.) çekiyoruz
            when (val response = repository.loadProfile()) {
                is Resource.Success -> {
                    // Backend verisini güvenle güncelle
                    _uiState.update { it.copy(
                        totalDecisionCount = response.data.data?.totalDecisionCount.toString(),
                        totalShareCount = response.data.data?.totalDecisionShareCount.toString(),
                        isLoading = false
                    ) }

                    // 2. Ardından DataStore'daki isim/resim gibi canlı akışları dinlemeye başlıyoruz
                    combine(
                        userRepository.getUserNameFlow(),
                        userRepository.getUserPictureFlow(),
                        userRepository.getUserEmailFlow()
                    ) { name, picture, email ->
                        // Herhangi bir veri değiştiğinde mevcut state'i (update ile) koruyarak güncelle
                        _uiState.update { currentState ->
                            currentState.copy(
                                userName = name ?: "Misafir",
                                photoUrl = picture ?: "",
                                email = email ?: "Bilinmiyor",
                                error = null
                            )
                        }
                    }.collectLatest {
                        // combine içindeki update işlemi yeterli, burası akışı canlı tutar
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(
                        error = response.message ,
                        isLoading = false
                    ) }
                }
                else -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
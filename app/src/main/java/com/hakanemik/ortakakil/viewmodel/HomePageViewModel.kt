package com.hakanemik.ortakakil.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.AiRequest
import com.hakanemik.ortakakil.entity.HomeUiState
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
class HomePageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Flow ile sürekli dinleme - DataStore değişirse UI otomatik güncellenir
                userRepository.getUserNameFlow().collectLatest { name ->
                    val userId = userRepository.getUserId()
                    _uiState.value = _uiState.value.copy(
                        userName = name ?: "Misafir",
                        userId = userId,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Bilinmeyen hata",
                    isLoading = false
                )
            }
        }
    }

    fun aiQuestion(){
       _uiState.value = _uiState.value.copy(isClicked = true)
    }

    fun onQuestionChange(value: String){
        _uiState.value = _uiState.value.copy(question = value)
    }
    fun onCategorySelected(category: String){
        _uiState.value = _uiState.value.copy(selected = category)
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logout()
                _uiState.value = HomeUiState(
                    userName = "Misafir",
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Çıkış yapılırken hata oluştu"
                )
            }
        }
    }
}
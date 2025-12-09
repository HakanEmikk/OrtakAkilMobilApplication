package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.AiRequest
import com.hakanemik.ortakakil.entity.AnswerUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnswerPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AnswerUiState())
    val uiState : StateFlow<AnswerUiState> = _uiState.asStateFlow()

    fun loadAnswer(question: String,category: String){
        _uiState.value = _uiState.value.copy(isLoading = true, question = question)
        viewModelScope.launch(Dispatchers.IO) {

            val response = repository.aiRequest(AiRequest(question =question,category=category))
            when(response){
                is Resource.Success ->{
                    _uiState.value = _uiState.value.copy(answer = response.data.data?.answer!!, isLoading = false)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(answer = "Bir hata oluştu. Lütfen tekrar deneyiniz", isLoading = false)
                }
                else -> {}
            }
        }
    }
}
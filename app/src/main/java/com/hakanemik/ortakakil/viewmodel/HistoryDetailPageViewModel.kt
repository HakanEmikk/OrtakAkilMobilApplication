package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.CommentResponse
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.entity.ShareRequest
import com.hakanemik.ortakakil.entity.AnswerUiEvent
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {

    private val _comments = MutableStateFlow<List<CommentResponse>>(emptyList())
    val comments: StateFlow<List<CommentResponse>> = _comments.asStateFlow()

    private val _shareNote = MutableStateFlow("")
    val shareNote: StateFlow<String> = _shareNote.asStateFlow()

    private val _uiEvent = Channel<AnswerUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun getComments(decisionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getComments(decisionId)) {
                is Resource.Success -> {
                    _comments.value = result.data.data ?: emptyList()
                }
                is Resource.Error -> {
                    // Handle error if needed
                }
                is Resource.Loading -> {
                    // Handle loading if needed
                }
            }
        }
    }

    fun onShareNoteChange(value: String) {
        _shareNote.value = value
    }

    fun shareHistoryItem(decisionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val shareRequest = ShareRequest(decisionId, _shareNote.value)
            val response = repository.shareAnswer(shareRequest)
            when (response) {
                is Resource.Success -> {
                    _uiEvent.send(AnswerUiEvent.ShareSuccess)
                    _shareNote.value = ""
                }
                is Resource.Error -> {
                    _uiEvent.send(AnswerUiEvent.ShareError)
                }
                else -> {}
            }
        }
    }

    fun unshareHistoryItem(decisionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.unshareAnswer(decisionId)
            when (response) {
                is Resource.Success -> {
                    _uiEvent.send(AnswerUiEvent.UnshareSuccess)
                }
                is Resource.Error -> {
                    _uiEvent.send(AnswerUiEvent.UnshareError)
                }
                else -> {}
            }
        }
    }
}

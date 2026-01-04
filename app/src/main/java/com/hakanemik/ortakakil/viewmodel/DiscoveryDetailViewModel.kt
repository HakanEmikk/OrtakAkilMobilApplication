package com.hakanemik.ortakakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryDetailViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {

    fun toggleLike(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.likeDecision(id)
        }
    }
}
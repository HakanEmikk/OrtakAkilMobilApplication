package com.hakanemik.ortakakil.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.helper.TimeUtils
import com.hakanemik.ortakakil.repo.TokenManager
import com.hakanemik.ortakakil.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        checkInitialNavigation()
    }


    private fun checkInitialNavigation() {
        viewModelScope.launch {

            delay(2000L)

            val accessToken = tokenManager.getToken()
            val refreshToken = tokenManager.getRefreshToken()
            val rememberMe = userRepository.getRememberMe()
            val userId = userRepository.getUserId()

            if (!rememberMe || userId == null || refreshToken == null) {
                _startDestination.value = "login_page"; return@launch
            }

            if (tokenManager.isRefreshExpired()) {
                _startDestination.value = "login_page"; return@launch
            }

            if (!tokenManager.isAccessExpired()) {
                _startDestination.value = "home_page"; return@launch
            }

            try {
                val ref = tokenManager.getRefreshToken()!!
                val res = userRepository.refreshWithRefreshToken(ref) // authApi
                res.data?.let { d ->
                    val accessExp = d.tokenExpiry
                    val refreshExp = d.refreshExpiry
                    val accessExpMs =TimeUtils.isoToMillis(accessExp)
                    val refreshExpMs = TimeUtils.isoToMillis(refreshExp)
                    tokenManager.saveTokens(d.accessToken, d.refreshToken, accessExpMs , refreshExpMs)
                    _startDestination.value = "home_page"; return@launch
                }
                _startDestination.value = "login_page"
            }catch (_: Exception){
                _startDestination.value = "login_page"
            }

        }
    }

   suspend fun logout() {
        tokenManager.clearTokens()
        userRepository.logout()
        _startDestination.value = "login_page"
    }
}
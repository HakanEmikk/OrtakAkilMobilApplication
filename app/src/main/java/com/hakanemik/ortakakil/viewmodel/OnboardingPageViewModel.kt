package com.hakanemik.ortakakil.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.LoginResponse
import com.hakanemik.ortakakil.entity.LoginUiState
import com.hakanemik.ortakakil.entity.OnboardingState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.helper.GoogleAuthHelper
import com.hakanemik.ortakakil.helper.TimeUtils
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import com.hakanemik.ortakakil.repo.TokenManager
import com.hakanemik.ortakakil.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repository: OrtakAkilDaoRepository,
    private val googleAuthHelper: GoogleAuthHelper,
    private val tokenManager: TokenManager
):ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingState())
    val uiState: StateFlow<OnboardingState> = _uiState.asStateFlow()
    @RequiresApi(Build.VERSION_CODES.O)
    fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(loginState = Resource.Loading) }

            try {
                val idToken = googleAuthHelper.getIdToken(context)
                when (val result = repository.googleWithLogin(idToken)) {
                    is Resource.Success -> {
                        val data = result.data.data
                        handleSuccessfulLogin(data)
                        _uiState.update { it.copy(loginState = result) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(loginState = result) }
                    }
                    else -> {}
                }
            } catch (e: NoCredentialException) {
                _uiState.update { it.copy(
                    loginState = Resource.Error("Bu cihazda Google hesabı yok.")
                ) }
            } catch (e: GetCredentialException) {
                _uiState.update { it.copy(
                    loginState = Resource.Error(e.message ?: "Google giriş iptal edildi.")
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    loginState = Resource.Error(e.message ?: "Google giriş başarısız.")
                ) }
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleSuccessfulLogin(data: LoginResponse?) {
        val user = data?.user
        val token = data?.accessToken
        val refreshToken = data?.refreshToken
        val now = System.currentTimeMillis()

        val accessExpMs = data?.tokenExpiry?.let { TimeUtils.isoToMillis(it) } ?: (now + 15 * 60 * 1000)
        val refreshExpMs = data?.refreshExpiry?.let { TimeUtils.isoToMillis(it) } ?: (now + 30L * 24 * 60 * 60 * 1000)

        if (user != null && token != null && refreshToken != null) {
            tokenManager.saveTokens(token, refreshToken, accessExpMs, refreshExpMs)
            userRepository.saveUserInfo(
                userId = user.id.toString(),
                userName = user.name,
                email = user.email,
                pictureUrl = user.pictureUrl
            )
            userRepository.saveRememberMe(true)
        }
    }
}
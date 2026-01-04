package com.hakanemik.ortakakil.viewmodel

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.LoginResponse
import com.hakanemik.ortakakil.entity.LoginUiState
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
class LoginPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository,
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
    private val googleAuthHelper: GoogleAuthHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(
            email = value,
            emailError = null
        ) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(
            password = value,
            passwordError = null
        ) }
    }

    fun onRememberMeChange(value: Boolean) {
        _uiState.update { it.copy(rememberMe = value) }
        viewModelScope.launch {
            userRepository.saveRememberMe(value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun login() {
        val currentState = _uiState.value
        val email = currentState.email.trim()
        val password = currentState.password

        var emailError: String? = null
        var passwordError: String? = null

        if (email.isBlank()) {
            emailError = "E-posta boş olamaz"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Geçerli bir e-posta giriniz"
        }

        if (password.isBlank()) {
            passwordError = "Şifre boş olamaz"
        } else if (password.length < 6) {
            passwordError = "Şifre en az 6 karakter olmalıdır"
        }

        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(
                emailError = emailError,
                passwordError = passwordError
            ) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loginState = Resource.Loading) }

            when (val result = repository.login(LoginRequest(email, password))) {
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
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.update { it.copy(loginState = Resource.Loading) }

            try {
                val idToken = googleAuthHelper.getIdToken()
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

    // Tekrar eden login mantığını temizlemek için yardımcı fonksiyon
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
        }
    }
}
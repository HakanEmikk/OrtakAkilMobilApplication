package com.hakanemik.ortakakil.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.LoginApiResponse
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.LoginUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import com.hakanemik.ortakakil.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class LoginPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        checkRememberedUser()
    }

    // Token kontrolü
    private fun checkRememberedUser() {
        viewModelScope.launch {
            val token = userRepository.getToken()
            val userId = userRepository.getUserId()

            if (token != null && userId != null) {
                _uiState.value = _uiState.value.copy(isAutoLogging = true)
            }
        }
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            emailError = null
        )
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            passwordError = null
        )
    }

    fun onRememberMeChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = value)
    }

    fun login() {
        val currentState = _uiState.value
        val email = currentState.email.trim()
        val password = currentState.password

        // Validasyon
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
            _uiState.value = currentState.copy(
                emailError = emailError,
                passwordError = passwordError
            )
            return
        }

        // API çağrısı
        viewModelScope.launch {
            _uiState.value = currentState.copy(
                loginState = Resource.Loading
            )

            val result = repository.login(LoginRequest(email, password))

            when (result) {
                is Resource.Success -> {
                    val user = result.data.data?.user
                    val token = result.data.data?.accessToken

                    // Kullanıcı bilgilerini kaydet
                    if (user != null && token != null) {
                        userRepository.saveToken(token)
                        userRepository.saveUserInfo(
                            userId = user.id.toString(),
                            userName = user.name ?: "Kullanıcı",
                            email = user.email
                        )
                    }

                    _uiState.value = currentState.copy(
                        loginState = result
                    )
                }
                is Resource.Error -> {
                    _uiState.value = currentState.copy(
                        loginState = result
                    )
                }
                else -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _uiState.value = LoginUiState() // Reset state
        }
    }
}
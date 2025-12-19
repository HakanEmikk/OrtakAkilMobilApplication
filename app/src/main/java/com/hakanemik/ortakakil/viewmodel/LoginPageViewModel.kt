package com.hakanemik.ortakakil.viewmodel

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.LoginRequest
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
        viewModelScope.launch {
            userRepository.saveRememberMe(_uiState.value.rememberMe)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
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

            when (val result = repository.login(LoginRequest(email, password))) {
                is Resource.Success -> {
                    val user = result.data.data?.user
                    val token = result.data.data?.accessToken
                    val refreshToken=result.data.data?.refreshToken
                    val now = System.currentTimeMillis()
                    val tokenExp= result.data.data?.tokenExpiry
                    val refreshExp= result.data.data?.refreshExpiry

                    val accessExpMs = tokenExp?.let { TimeUtils.isoToMillis(it) }
                        ?: (now + 15*60*1000) // backend "expiresIn" yollamıyorsa fallback

                    val refreshExpMs = refreshExp?.let { TimeUtils.isoToMillis(it) }
                        ?: (now + 30L*24*60*60*1000)


                    // Kullanıcı bilgilerini kaydet
                    if (user != null && token != null && refreshToken != null) {
                        tokenManager.saveTokens(token,refreshToken,accessExpMs,refreshExpMs)
                        userRepository.saveUserInfo(
                            userId = user.id.toString(),
                            userName = user.name,
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loginState = Resource.Loading)

            try {
                val idToken = googleAuthHelper.getIdToken()   // <-- hata burada patlıyor
                when (val result = repository.googleWithLogin(idToken)) {
                    is Resource.Success -> {
                        val user = result.data.data?.user
                        val token = result.data.data?.accessToken
                        val refreshToken=result.data.data?.refreshToken
                        val now = System.currentTimeMillis()
                        val tokenExp= result.data.data?.tokenExpiry
                        val refreshExp= result.data.data?.refreshExpiry

                        val accessExpMs = tokenExp?.let { TimeUtils.isoToMillis(it) }
                            ?: (now + 15*60*1000) // backend "expiresIn" yollamıyorsa fallback

                        val refreshExpMs = refreshExp?.let { TimeUtils.isoToMillis(it) }
                            ?: (now + 30L*24*60*60*1000)


                        // Kullanıcı bilgilerini kaydet
                        if (user != null && token != null && refreshToken != null) {
                            tokenManager.saveTokens(token,refreshToken,accessExpMs,refreshExpMs)
                            userRepository.saveUserInfo(
                                userId = user.id.toString(),
                                userName = user.name,
                                email = user.email
                            )
                        }

                        _uiState.value = _uiState.value.copy(
                            loginState = result
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(loginState = result)
                    }
                    else -> {}
                }
            } catch (e: NoCredentialException) {
                _uiState.value = _uiState.value.copy(
                    loginState = Resource.Error("Bu cihazda Google hesabı yok. Ayarlardan Google hesabı ekleyip tekrar deneyin.")
                )
            } catch (e: GetCredentialException) {
                _uiState.value = _uiState.value.copy(
                    loginState = Resource.Error(e.message ?: "Google giriş iptal edildi.")
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loginState = Resource.Error(e.message ?: "Google giriş başarısız.")
                )
            }
        }
    }
}
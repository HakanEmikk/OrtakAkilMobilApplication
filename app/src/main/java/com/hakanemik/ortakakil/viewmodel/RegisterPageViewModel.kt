package com.hakanemik.ortakakil.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.RegisterRequest
import com.hakanemik.ortakakil.entity.RegisterUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class RegisterPageViewModel @Inject constructor(
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // ---- Inputs ----
    fun onNameChange(value: String) {
        _uiState.update { it.copy(
            name = value,
            nameError = null
        ) }
    }

    fun onSurnameChange(value: String) {
        _uiState.update { it.copy(
            surname = value,
            surnameError = null
        ) }
    }

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

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(
            confirmPassword = value,
            confirmPasswordError = null
        ) }
    }

    fun register() {
        // Doğrulama anındaki güncel verileri çekiyoruz
        val currentState = _uiState.value
        val name = currentState.name
        val surname = currentState.surname
        val email = currentState.email.trim()
        val password = currentState.password
        val confirmPassword = currentState.confirmPassword

        var nameError: String? = null
        var surnameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        // --- Validasyonlar ---
        if (name.isBlank()) {
            nameError = "İsim boş olamaz"
        } else if (name.length < 2) {
            nameError = "İsim en az 2 karakter olmalıdır"
        } else if (!name.matches(Regex("^[a-zA-ZığüşöçİĞÜŞÖÇ ]+$"))) {
            nameError = "İsim sadece harf içermelidir"
        }

        if (surname.isBlank()) {
            surnameError = "Soyisim boş olamaz"
        } else if (surname.length < 2) {
            surnameError = "Soyisim en az 2 karakter olmalıdır"
        } else if (!surname.matches(Regex("^[a-zA-ZığüşöçİĞÜŞÖÇ ]+$"))) {
            surnameError = "Soyisim sadece harf içermelidir"
        }

        if (email.isBlank()) {
            emailError = "E-posta boş olamaz"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Geçerli bir e-posta giriniz"
        }

        if (password.isBlank()) {
            passwordError = "Şifre boş olamaz"
        } else if (password.length < 6) {
            passwordError = "Şifre en az 6 karakter olmalıdır"
        } else if (password.length > 20) {
            passwordError = "Şifre en fazla 20 karakter olmalıdır"
        } else if (!password.matches(Regex(".*[A-Za-z].*"))) {
            passwordError = "Şifre en az bir harf içermelidir"
        } else if (!password.matches(Regex(".*\\d.*"))) {
            passwordError = "Şifre en az bir rakam içermelidir"
        }

        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Şifre tekrarı boş olamaz"
        } else if (password != confirmPassword) {
            confirmPasswordError = "Şifreler eşleşmiyor"
        }

        // Hata varsa state'i güncelle ve dur
        if (nameError != null || surnameError != null || emailError != null ||
            passwordError != null || confirmPasswordError != null) {
            _uiState.update { it.copy(
                nameError = nameError,
                surnameError = surnameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
            ) }
            return
        }

        // API çağrısı
        viewModelScope.launch {
            _uiState.update { it.copy(registerState = Resource.Loading) }

            val req = RegisterRequest(name, surname, email, password, confirmPassword)
            val result = repository.register(req)

            _uiState.update { it.copy(registerState = result) }
        }
    }

    fun clearUiState() {
        _uiState.update { RegisterUiState() }
    }
}
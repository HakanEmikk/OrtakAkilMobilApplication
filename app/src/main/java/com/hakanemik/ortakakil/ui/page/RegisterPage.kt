package com.hakanemik.ortakakil.ui.page

import LoginButton
import LoginTextFields
import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.RegisterRequest
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.ui.util.DeviceSize
import com.hakanemik.ortakakil.ui.util.currentDeviceSizeHelper
import com.hakanemik.ortakakil.ui.util.responsive
import com.hakanemik.ortakakil.ui.util.responsiveSp
import com.hakanemik.ortakakil.viewmodel.LoginPageViewModel
import com.hakanemik.ortakakil.viewmodel.RegisterPageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(navController: NavController, snackbarHostState: SnackbarHostState) {
    val viewModel: RegisterPageViewModel = viewModel()
    val uiState by viewModel.uiState.observeAsState()
    val deviceSize = currentDeviceSizeHelper()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {  innerPadding ->

        when (deviceSize) {
            DeviceSize.Compact -> {
                // Telefon - Dikey layout
                CompactRegisterLayout(deviceSize,viewModel, navController)
            }
            DeviceSize.Medium -> {
                // Tablet - Ortalanmış layout
                MediumRegisterLayout(deviceSize,viewModel, navController)
            }
            DeviceSize.Expanded -> {
                // Masaüstü - Yatay split layout
                ExpandedRegisterLayout(deviceSize,viewModel, navController)
            }
        }
        HandleUIState(uiState, snackbarHostState, navController)

    }
}

@Composable
private fun CompactRegisterLayout(
    deviceSize: DeviceSize,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterContent(deviceSize,viewModel, navController)
    }
}

@Composable
private fun MediumRegisterLayout(
    deviceSize: DeviceSize,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark))
    ) {
        // Sol taraf - boş alan
        Spacer(modifier = Modifier.weight(0.15f))

        // Orta - Register formu
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterContent(deviceSize,viewModel, navController)
        }

        // Sağ taraf - boş alan
        Spacer(modifier = Modifier.weight(0.15f))
    }
}

@Composable
private fun ExpandedRegisterLayout(
    deviceSize: DeviceSize,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark))
    ) {
        // Sol taraf - Logo/Brand alanı
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ortak_akil_logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp.responsive(150.dp, 200.dp, 180.dp, deviceSize))
                    .clip(RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Akıllı kararlar ver, topluluğa katıl",
                fontSize = 15f.responsiveSp(15f, 18f, 20f, deviceSize),
                color = colorResource(id = R.color.text_primary),
                textAlign = TextAlign.Center
            )
        }

        // Sağ taraf - Register formu
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(48.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterFormOnly(deviceSize,viewModel, navController)
        }
    }
}

@Composable
fun RegisterContent(
    deviceSize: DeviceSize,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var surnameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    Spacer(modifier = Modifier.height(100.dp.responsive(100.dp, 60.dp, 40.dp, deviceSize)))

    Image(
        painter = painterResource(id = R.drawable.ortak_akil_logo),
        contentDescription = "logo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp.responsive(150.dp, 200.dp, 180.dp, deviceSize))
            .clip(RoundedCornerShape(20.dp))
    )

    Spacer(modifier = Modifier.height(20.dp.responsive(20.dp, 30.dp, 24.dp, deviceSize)))

    Text(
        text = "Yeni bir hesap oluştur",
        fontSize = 18f.responsiveSp(18f, 22f, 24f, deviceSize),
        color = colorResource(id = R.color.text_primary),
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(25.dp.responsive(25.dp, 35.dp, 32.dp, deviceSize)))

    RegisterForm(
        name = name,
        surname = surname,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        nameError = nameError,
        surnameError = surnameError,
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError,
        deviceSize = deviceSize,
        onNameChange = {
            name = it
            nameError = null
        },
        onSurnameChange = {
            surname = it
            surnameError = null
        },
        onEmailChange = {
            email = it
            emailError = null
        },
        onPasswordChange = {
            password = it
            passwordError = null
        },
        onConfirmPasswordChange = {
            confirmPassword = it
            confirmPasswordError = null
        },
        onRegister = { nameErr, surnameErr, emailErr, passErr, confirmPassErr ->
            nameError = nameErr
            surnameError = surnameErr
            emailError = emailErr
            passwordError = passErr
            confirmPasswordError = confirmPassErr

            if (nameErr == null && surnameErr == null && emailErr == null &&
                passErr == null && confirmPassErr == null) {

                 val registerRequest = RegisterRequest(name, surname, email, password,confirmPassword)
                 viewModel.register(registerRequest)
            }
        },
        onNavigateToLogin = { navController.navigate("login_page") }
    )

    Spacer(modifier = Modifier.height(50.dp.responsive(50.dp, 60.dp, 40.dp, deviceSize)))
}

@Composable
private fun RegisterFormOnly(
    deviceSize: DeviceSize,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var surnameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    Text(
        text = "Yeni bir hesap oluştur",
        fontSize = 18.sp,
        color = colorResource(id = R.color.text_primary),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 32.dp)
    )

    RegisterForm(
        name = name,
        surname = surname,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        nameError = nameError,
        surnameError = surnameError,
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError,
        deviceSize = deviceSize,
        onNameChange = {
            name = it
            nameError = null
        },
        onSurnameChange = {
            surname = it
            surnameError = null
        },
        onEmailChange = {
            email = it
            emailError = null
        },
        onPasswordChange = {
            password = it
            passwordError = null
        },
        onConfirmPasswordChange = {
            confirmPassword = it
            confirmPasswordError = null
        },
        onRegister = { nameErr, surnameErr, emailErr, passErr, confirmPassErr ->
            nameError = nameErr
            surnameError = surnameErr
            emailError = emailErr
            passwordError = passErr
            confirmPasswordError = confirmPassErr

            if (nameErr == null && surnameErr == null && emailErr == null &&
                passErr == null && confirmPassErr == null) {
                val registerRequest = RegisterRequest(name, surname, email, password,confirmPassword)
                viewModel.register(registerRequest)
            }
        },
        onNavigateToLogin = { navController.navigate("login_page") }
    )
}

@Composable
private fun RegisterForm(
    name: String,
    surname: String,
    email: String,
    password: String,
    confirmPassword: String,
    nameError: String?,
    surnameError: String?,
    emailError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    deviceSize: DeviceSize,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: (String?, String?, String?, String?, String?) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Name TextField with Error
    Column {
        LoginTextFields(
            onValueChange = onNameChange,
            label = "İsim",
            value = name,
            deviceSize = deviceSize
        )
        if (nameError != null) {
            Text(
                text = nameError,
                color = Color.Red,
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Surname TextField with Error
    Column {
        LoginTextFields(
            onValueChange = onSurnameChange,
            label = "Soyisim",
            value = surname,
            deviceSize = deviceSize
        )
        if (surnameError != null) {
            Text(
                text = surnameError,
                color = Color.Red,
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Email TextField with Error
    Column {
        LoginTextFields(
            onValueChange = onEmailChange,
            label = "E-mail",
            value = email,
            deviceSize = deviceSize
        )
        if (emailError != null) {
            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Password TextField with Error
    Column {
        LoginTextFields(
            onValueChange = onPasswordChange,
            label = "Şifre",
            value = password,
            deviceSize = deviceSize
        )
        if (passwordError != null) {
            Text(
                text = passwordError,
                color = Color.Red,
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Confirm Password TextField with Error
    Column {
        LoginTextFields(
            onValueChange = onConfirmPasswordChange,
            label = "Şifre Tekrar",
            value = confirmPassword,
            deviceSize = deviceSize
        )
        if (confirmPasswordError != null) {
            Text(
                text = confirmPasswordError,
                color = Color.Red,
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(30.dp.responsive(30.dp, 35.dp, 40.dp, deviceSize)))

    // Register Button with Validation
    LoginButton(
        onClick = {
            var nameErr: String? = null
            var surnameErr: String? = null
            var emailErr: String? = null
            var passErr: String? = null
            var confirmPassErr: String? = null

            // Name validation
            if (name.isBlank()) {
                nameErr = "İsim boş olamaz"
            } else if (name.length < 2) {
                nameErr = "İsim en az 2 karakter olmalıdır"
            } else if (!name.matches(Regex("^[a-zA-ZığüşöçİĞÜŞÖÇ ]+$"))) {
                nameErr = "İsim sadece harf içermelidir"
            }

            // Surname validation
            if (surname.isBlank()) {
                surnameErr = "Soyisim boş olamaz"
            } else if (surname.length < 2) {
                surnameErr = "Soyisim en az 2 karakter olmalıdır"
            } else if (!surname.matches(Regex("^[a-zA-ZığüşöçİĞÜŞÖÇ ]+$"))) {
                surnameErr = "Soyisim sadece harf içermelidir"
            }

            // Email validation
            if (email.isBlank()) {
                emailErr = "E-posta boş olamaz"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailErr = "Geçerli bir e-posta giriniz"
            }

            // Password validation
            if (password.isBlank()) {
                passErr = "Şifre boş olamaz"
            } else if (password.length < 6) {
                passErr = "Şifre en az 6 karakter olmalıdır"
            } else if (password.length > 20) {
                passErr = "Şifre en fazla 20 karakter olmalıdır"
            } else if (!password.matches(Regex(".*[A-Za-z].*"))) {
                passErr = "Şifre en az bir harf içermelidir"
            } else if (!password.matches(Regex(".*\\d.*"))) {
                passErr = "Şifre en az bir rakam içermelidir"
            }

            // Confirm password validation
            if (confirmPassword.isBlank()) {
                confirmPassErr = "Şifre tekrarı boş olamaz"
            } else if (password != confirmPassword) {
                confirmPassErr = "Şifreler eşleşmiyor"
            }

            onRegister(nameErr, surnameErr, emailErr, passErr, confirmPassErr)
        },
        colorRes = R.color.primary_purple,
        borderColor = R.color.border_transparent,
        label = "Kayıt Ol",
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(20.dp.responsive(20.dp, 24.dp, 28.dp, deviceSize)))

    // Login Navigation Button
    TextButton(
        onClick = onNavigateToLogin,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Zaten hesabın var mı? Giriş Yap",
            color = colorResource(id = R.color.text_primary),
            fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
        )
    }
}
@Composable
private fun HandleUIState(
    uiState: Resource<*>?,
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    when (uiState) {
        is Resource.Success -> {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar("Kayıt Başarılı")
            }
            navController.navigate("login_page")
        }
        is Resource.Error -> {
            val errorMessage = uiState.message ?: "Bir hata oluştu"
            LaunchedEffect(errorMessage) {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
        else -> {}
    }
}
package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.entity.RegisterUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.ui.utils.AuthButton
import com.hakanemik.ortakakil.ui.utils.AuthTextFields
import com.hakanemik.ortakakil.viewmodel.RegisterPageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit
) {
    val viewModel: RegisterPageViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deviceSize = currentDeviceSizeHelper()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterContent(deviceSize, uiState, viewModel, navController)
        }
    }

    HandleUIState(uiState.registerState, navController, onShowSnackbar) { viewModel.clearUiState() }
}

@Composable
fun RegisterContent(
    deviceSize: DeviceSize,
    uiState: RegisterUiState,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 10.dp, 12.dp, deviceSize)))

    // Logo Area with Glow
    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(140.dp.responsive(140.dp, 160.dp, 150.dp, deviceSize))
                .clip(RoundedCornerShape(20.dp))
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "Yeni Hesap Oluştur",
        fontSize = 26.sp,
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )

    Text(
        text = "Topluluğa katıl ve fikirlerini paylaşmaya başla",
        fontSize = 14.sp,
        color = colorResource(id = R.color.text_muted),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp)
    )

    Spacer(modifier = Modifier.height(40.dp))

    RegisterForm(
        uiState = uiState,
        deviceSize = deviceSize,
        onNameChange = viewModel::onNameChange,
        onSurnameChange = viewModel::onSurnameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegister = { viewModel.register() },
        onNavigateToLogin = { navController.navigate("login_page") },
    )
}

@Composable
private fun RegisterForm(
    uiState: RegisterUiState,
    deviceSize: DeviceSize,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val isLoading = uiState.registerState is Resource.Loading

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // İsim & Soyisim Yana Yana (Daha modern bir görünüm için)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                AuthTextFields(onValueChange = onNameChange, label = "İsim", value = uiState.name, deviceSize = deviceSize)
                if (uiState.nameError != null) ErrorText(uiState.nameError)
            }
            Column(modifier = Modifier.weight(1f)) {
                AuthTextFields(onValueChange = onSurnameChange, label = "Soyisim", value = uiState.surname, deviceSize = deviceSize)
                if (uiState.surnameError != null) ErrorText(uiState.surnameError)
            }
        }

        // Email
        Column {
            AuthTextFields(onValueChange = onEmailChange, label = "E-posta", value = uiState.email, deviceSize = deviceSize)
            if (uiState.emailError != null) ErrorText(uiState.emailError)
        }

        // Şifre
        Column {
            AuthTextFields(onValueChange = onPasswordChange, label = "Şifre", value = uiState.password, deviceSize = deviceSize, isPassword = true)
            if (uiState.passwordError != null) ErrorText(uiState.passwordError)
        }

        // Şifre Tekrar
        Column {
            AuthTextFields(onValueChange = onConfirmPasswordChange, label = "Şifre Tekrar", value = uiState.confirmPassword, deviceSize = deviceSize, isPassword = true)
            if (uiState.confirmPasswordError != null) ErrorText(uiState.confirmPasswordError)
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    AuthButton(
        onClick = onRegister,
        colorRes = R.color.primary_purple,
        label = "Kayıt Ol",
        deviceSize = deviceSize,
        isLoading = isLoading
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(
        onClick = onNavigateToLogin,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Zaten bir hesabın var mı? ", color = colorResource(id = R.color.text_muted), fontSize = 14.sp)
            Text("Giriş Yap", color = colorResource(id = R.color.primary_purple), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }

    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun ErrorText(text: String) {
    Text(
        text = text,
        color = colorResource(id = R.color.error),
        fontSize = 11.sp,
        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
    )
}

@Composable
private fun HandleUIState(
    uiState: Resource<*>?,
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    onConsumed: () -> Unit
) {
    LaunchedEffect(uiState) {
        when (uiState) {
            is Resource.Success -> {
                onShowSnackbar("Aramıza hoş geldin!", SnackbarType.SUCCESS)
                onConsumed()
                navController.navigate("login_page") {
                    popUpTo("register_page") { inclusive = true }
                }
            }
            is Resource.Error -> {
                onShowSnackbar(uiState.message, SnackbarType.ERROR)
                onConsumed()
            }
            else -> Unit
        }
    }
}
package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.LoginUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.ui.utils.AuthButton
import com.hakanemik.ortakakil.ui.utils.AuthTextFields
import com.hakanemik.ortakakil.viewmodel.LoginPageViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginPage(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: LoginPageViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deviceSize = currentDeviceSizeHelper()

    // Auto-login check
    LaunchedEffect(uiState.isAutoLogging) {
        if (uiState.isAutoLogging) {
            navController.navigate("home_page") {

            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { _ ->
        when (deviceSize) {
            DeviceSize.Compact -> CompactLoginLayout(deviceSize, uiState, viewModel, navController)
            DeviceSize.Medium -> MediumLoginLayout(deviceSize, uiState, viewModel, navController)
            DeviceSize.Expanded -> ExpandedLoginLayout(deviceSize, uiState, viewModel, navController)
        }

        // Handle UI State
        HandleUIState(uiState.loginState, snackbarHostState, navController)
    }
}

@Composable
private fun CompactLoginLayout(
    deviceSize: DeviceSize,
    uiState: LoginUiState,
    viewModel: LoginPageViewModel,
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
        LoginContent(deviceSize, uiState, viewModel, navController)
    }
}

@Composable
private fun MediumLoginLayout(
    deviceSize: DeviceSize,
    uiState: LoginUiState,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark))
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginContent(deviceSize, uiState, viewModel, navController)
        }
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
private fun ExpandedLoginLayout(
    deviceSize: DeviceSize,
    uiState: LoginUiState,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark))
    ) {
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
                    .size(200.dp.responsive(200.dp, 250.dp, 200.dp, deviceSize))
                    .clip(RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Akıllı kararlar ver, topluluğa katıl",
                fontSize = 15f.responsiveSp(15f, 18f, 20f, deviceSize),
                color = colorResource(id = R.color.text_primary),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(48.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginFormOnly(deviceSize, uiState, viewModel, navController)
        }
    }
}

@Composable
fun LoginContent(
    deviceSize: DeviceSize,
    uiState: LoginUiState,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(120.dp.responsive(120.dp, 60.dp, 40.dp, deviceSize)))

    Image(
        painter = painterResource(id = R.drawable.ortak_akil_logo),
        contentDescription = "Logo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(200.dp.responsive(200.dp, 250.dp, 200.dp, deviceSize))
            .clip(RoundedCornerShape(20.dp))
    )

    Spacer(modifier = Modifier.height(20.dp.responsive(20.dp, 30.dp, 24.dp, deviceSize)))

    Text(
        text = "Akıllı kararlar ver, topluluğa katıl",
        fontSize = 15f.responsiveSp(15f, 18f, 20f, deviceSize),
        color = colorResource(id = R.color.text_primary),
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(30.dp.responsive(30.dp, 40.dp, 32.dp, deviceSize)))

    LoginForm(
        uiState = uiState,
        deviceSize = deviceSize,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRememberMeChange = viewModel::onRememberMeChange,
        onLogin = viewModel::login,
        onNavigateToRegister = { navController.navigate("register_page") }
    )

    Spacer(modifier = Modifier.height(50.dp.responsive(50.dp, 60.dp, 40.dp, deviceSize)))
}

@Composable
private fun LoginFormOnly(
    deviceSize: DeviceSize,
    uiState: LoginUiState,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    Text(
        text = "Giriş Yap",
        fontSize = 32.sp,
        color = colorResource(id = R.color.text_primary),
        modifier = Modifier.padding(bottom = 32.dp)
    )

    LoginForm(
        uiState = uiState,
        deviceSize = deviceSize,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRememberMeChange = viewModel::onRememberMeChange,
        onLogin = viewModel::login,
        onNavigateToRegister = { navController.navigate("register_page") }
    )
}

@Composable
private fun LoginForm(
    uiState: LoginUiState,
    deviceSize: DeviceSize,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val isLoading = uiState.loginState is Resource.Loading

    // Email
    Column {
        AuthTextFields(
            onValueChange = onEmailChange,
            label = "E-mail",
            value = uiState.email,
            deviceSize = deviceSize
        )
        if (uiState.emailError != null) {
            Text(
                text = uiState.emailError,
                color = colorResource(id = R.color.error),
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(21.dp.responsive(21.dp, 24.dp, 28.dp, deviceSize)))

    // Password
    Column {
        AuthTextFields(
            onValueChange = onPasswordChange,
            label = "Şifre",
            value = uiState.password,
            deviceSize = deviceSize
        )
        if (uiState.passwordError != null) {
            Text(
                text = uiState.passwordError,
                color = colorResource(id = R.color.error),
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 20.dp, 24.dp, deviceSize)))

    // Remember Me
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.rememberMe,
                onCheckedChange = onRememberMeChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.primary_purple)
                )
            )
            Text(
                text = "Beni Hatırla",
                color = colorResource(id = R.color.text_primary),
                fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
            )
        }
        Text(
            text = "Şifremi Unuttum",
            color = colorResource(id = R.color.text_primary),
            fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
        )
    }

    Spacer(modifier = Modifier.height(35.dp.responsive(35.dp, 40.dp, 48.dp, deviceSize)))

    AuthButton(
        onClick = onLogin,
        colorRes = R.color.primary_purple,
        isLoading = isLoading,
        borderColor = R.color.border_transparent,
        label = "Giriş Yap",
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(17.dp.responsive(17.dp, 20.dp, 24.dp, deviceSize)))

    AuthButton(
        onClick = onNavigateToRegister,
        colorRes = R.color.transparent,
        borderColor = R.color.border_default,
        label = "Kayıt Ol",
        deviceSize = deviceSize
    )
}


@Composable
private fun HandleUIState(
    loginState: Resource<*>?,
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    LaunchedEffect(loginState) {
        when (loginState) {
            is Resource.Success -> {
                launch { snackbarHostState.showSnackbar("Giriş başarılı") }
                navController.navigate("home_page") {
                    popUpTo("login_page") { inclusive = true }
                }
            }
            is Resource.Error -> {
                snackbarHostState.showSnackbar(loginState.message ?: "Bir hata oluştu")
            }
            else -> {}
        }
    }
}
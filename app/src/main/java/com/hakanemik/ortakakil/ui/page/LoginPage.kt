package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
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

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun LoginPage(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: LoginPageViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deviceSize = currentDeviceSizeHelper()

        when (deviceSize) {
            DeviceSize.Compact -> CompactLoginLayout(deviceSize, uiState, viewModel, navController)
            DeviceSize.Medium -> MediumLoginLayout(deviceSize, uiState, viewModel, navController)
            DeviceSize.Expanded -> ExpandedLoginLayout(deviceSize, uiState, viewModel, navController)
        }

        // Handle UI State
        HandleUIState(uiState.loginState, snackbarHostState, navController)
    }


@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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
            .size(160.dp.responsive(160.dp, 200.dp, 160.dp, deviceSize))
            .clip(RoundedCornerShape(20.dp))
    )

    Spacer(modifier = Modifier.height(24.dp.responsive(24.dp, 30.dp, 28.dp, deviceSize)))

    Text(
        text = "Akıllı kararlar ver",
        fontSize = 28f.responsiveSp(28f, 32f, 36f, deviceSize),
        color = colorResource(id = R.color.text_primary),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "topluluğa katıl",
        fontSize = 16f.responsiveSp(16f, 18f, 20f, deviceSize),
        color = colorResource(id = R.color.text_secondary),
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(40.dp.responsive(40.dp, 50.dp, 48.dp, deviceSize)))

    LoginForm(
        uiState = uiState,
        deviceSize = deviceSize,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRememberMeChange = viewModel::onRememberMeChange,
        onLogin = viewModel::login,
        onNavigateToRegister = { navController.navigate("register_page") }
    )

    Spacer(modifier = Modifier.height(40.dp.responsive(40.dp, 50.dp, 40.dp, deviceSize)))
}

@RequiresApi(Build.VERSION_CODES.O)
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
        fontWeight = FontWeight.Bold,
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
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp.responsive(20.dp, 24.dp, 28.dp, deviceSize)))

    // Password
    Column {
        AuthTextFields(
            onValueChange = onPasswordChange,
            label = "Şifre",
            value = uiState.password,
            deviceSize = deviceSize,
            isPassword = true
        )
        if (uiState.passwordError != null) {
            Text(
                text = uiState.passwordError,
                color = colorResource(id = R.color.error),
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp.responsive(16.dp, 20.dp, 24.dp, deviceSize)))

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
                    checkedColor = colorResource(id = R.color.primary_purple),
                    uncheckedColor = colorResource(id = R.color.text_secondary)
                ),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Beni Hatırla",
                color = colorResource(id = R.color.text_secondary),
                fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
            )
        }
        Text(
            text = "Şifremi Unuttum",
            color = colorResource(id = R.color.primary_purple),
            fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize),
            fontWeight = FontWeight.Medium
        )
    }

    Spacer(modifier = Modifier.height(32.dp.responsive(32.dp, 40.dp, 48.dp, deviceSize)))

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
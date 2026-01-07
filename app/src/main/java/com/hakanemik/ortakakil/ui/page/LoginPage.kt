package com.hakanemik.ortakakil.ui.page


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import com.hakanemik.ortakakil.entity.LoginUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.ui.utils.AuthButton
import com.hakanemik.ortakakil.ui.utils.AuthTextFields
import com.hakanemik.ortakakil.viewmodel.LoginPageViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginPage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: LoginPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deviceSize = currentDeviceSizeHelper()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.background_dark))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginContent(deviceSize, uiState, viewModel, navController)
        }
    }

    HandleUIState(uiState.loginState, navController, onShowSnackbar)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginContent(
    deviceSize: DeviceSize,
    uiState: LoginUiState,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(80.dp.responsive(80.dp, 60.dp, 40.dp, deviceSize)))

    // Logo Area
    Box(contentAlignment = Alignment.Center) {
        // Soft Glow behind logo
        Box(modifier = Modifier.size(140.dp).background(
            Brush.radialGradient(listOf(colorResource(R.color.primary_purple).copy(0.15f), Color.Transparent))
        ))
        Image(
            painter = painterResource(id = R.drawable.ortak_akil_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(36.dp))
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = "Ortak Akıl'a Hoş Geldin",
        fontSize = 28.sp,
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )

    Text(
        text = "Akıllı kararlar için topluluğun gücünü keşfet",
        fontSize = 14.sp,
        color = colorResource(id = R.color.text_muted),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp)
    )

    Spacer(modifier = Modifier.height(48.dp))

    LoginForm(
        uiState = uiState,
        deviceSize = deviceSize,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRememberMeChange = viewModel::onRememberMeChange,
        onLogin = viewModel::login,
        onNavigateToRegister = { navController.navigate("register_page") },
        onGoogleSignIn = viewModel::loginWithGoogle
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
    onNavigateToRegister: () -> Unit,
    onGoogleSignIn: () -> Unit
) {
    val isLoading = uiState.loginState is Resource.Loading

    AuthTextFields(
        onValueChange = onEmailChange,
        label = "E-posta Adresi",
        value = uiState.email,
        deviceSize = deviceSize
    )

    if (uiState.emailError != null) {
        Text(uiState.emailError, color = colorResource(R.color.error), fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
    }

    Spacer(modifier = Modifier.height(20.dp))

    AuthTextFields(
        onValueChange = onPasswordChange,
        label = "Şifre",
        value = uiState.password,
        deviceSize = deviceSize,
        isPassword = true
    )

    if (uiState.passwordError != null) {
        Text(uiState.passwordError, color = colorResource(R.color.error), fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
    }

    Spacer(modifier = Modifier.height(32.dp))

    AuthButton(
        onClick = onLogin,
        colorRes = R.color.primary_purple,
        label = "Giriş Yap",
        isLoading = isLoading,
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text("VEYA", color = colorResource(R.color.text_muted), fontSize = 12.sp, fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(24.dp))

    GoogleSignInButton(onClick = onGoogleSignIn, isLoading = isLoading, deviceSize = deviceSize)

    Spacer(modifier = Modifier.height(20.dp))

    AuthButton(
        onClick = onNavigateToRegister,
        colorRes = R.color.transparent,
        borderColor = R.color.border_default,
        label = "Hesabın yok mu? Kayıt Ol",
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
private fun GoogleSignInButton(onClick: () -> Unit, isLoading: Boolean, deviceSize: DeviceSize) {
    OutlinedButton(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White.copy(0.05f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.google), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Google ile Devam Et", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun HandleUIState(loginState: Resource<*>?, navController: NavController, onShowSnackbar: (String, SnackbarType) -> Unit) {
    LaunchedEffect(loginState) {
        when (loginState) {
            is Resource.Success -> {
                onShowSnackbar("Başarıyla giriş yapıldı", SnackbarType.SUCCESS)
                navController.navigate("home_page") { popUpTo("login_page") { inclusive = true } }
            }
            is Resource.Error -> onShowSnackbar(loginState.message, SnackbarType.ERROR)
            else -> {}
        }
    }
}
package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
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
import com.hakanemik.ortakakil.entity.RegisterUiState
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.ui.utils.AuthButton
import com.hakanemik.ortakakil.ui.utils.AuthTextFields
import com.hakanemik.ortakakil.viewmodel.RegisterPageViewModel



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(navController: NavController, snackbarHostState: SnackbarHostState) {
    val viewModel: RegisterPageViewModel =  hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deviceSize = currentDeviceSizeHelper()

        when (deviceSize) {
            DeviceSize.Compact -> CompactRegisterLayout(deviceSize,uiState, viewModel, navController)
            DeviceSize.Medium -> MediumRegisterLayout(deviceSize,uiState, viewModel, navController)
            DeviceSize.Expanded -> ExpandedRegisterLayout(deviceSize,uiState, viewModel, navController)
        }


        HandleUIState(uiState.registerState, snackbarHostState, navController) { viewModel.clearUiState() }
    }

@Composable
private fun CompactRegisterLayout(
    deviceSize: DeviceSize,
    uiState: RegisterUiState,
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
        RegisterContent(deviceSize,uiState, viewModel, navController)
    }
}


@Composable
private fun MediumRegisterLayout(
    deviceSize: DeviceSize,
    uiState: RegisterUiState,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark))
    ) {
        Spacer(modifier = Modifier.weight(0.15f))
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterContent(deviceSize,uiState, viewModel, navController)
        }
        Spacer(modifier = Modifier.weight(0.15f))
    }
}
@Composable
private fun ExpandedRegisterLayout(
    deviceSize: DeviceSize,
    uiState: RegisterUiState,
    viewModel: RegisterPageViewModel,
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


        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(48.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterFormOnly(deviceSize,uiState,viewModel, navController)
        }
    }
}
@Composable
fun RegisterContent(
    deviceSize: DeviceSize,
    uiState: RegisterUiState,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {
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
        uiState=uiState,
        deviceSize = deviceSize,
        onNameChange = viewModel::onNameChange,
        onSurnameChange = viewModel::onSurnameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegister = {viewModel.register()},
        onNavigateToLogin = { navController.navigate("login_page") },
    )


    Spacer(modifier = Modifier.height(50.dp.responsive(50.dp, 60.dp, 40.dp, deviceSize)))
}
@Composable
private fun RegisterFormOnly(
    deviceSize: DeviceSize,
    uiState: RegisterUiState,
    viewModel: RegisterPageViewModel,
    navController: NavController
) {



    Text(
        text = "Yeni bir hesap oluştur",
        fontSize = 18.sp,
        color = colorResource(id = R.color.text_primary),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 32.dp)
    )
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
)
{
    val isLoading = uiState.registerState is Resource.Loading
    Column {
        Column {
            AuthTextFields(
                onValueChange = onNameChange,
                label = "İsim",
                value = uiState.name,
                deviceSize = deviceSize
            )
            if (uiState.nameError != null) {
                Text(
                    text = uiState.nameError,
                    color = colorResource(id = R.color.error),
                    fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
        }


    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))


// Surname
    Column {
        AuthTextFields(
            onValueChange = onSurnameChange,
            label = "Soyisim",
            value = uiState.surname,
            deviceSize = deviceSize
        )
        if (uiState.surnameError != null) {
            Text(
                text =  uiState.surnameError,
                color = colorResource(id = R.color.error),
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }


    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))
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
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }


    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))


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
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }


    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))
    // Confirm Password
    Column {
        AuthTextFields(
            onValueChange = onConfirmPasswordChange,
            label = "Şifre Tekrar",
            value = uiState.confirmPassword,
            deviceSize = deviceSize
        )
        if (uiState.confirmPasswordError != null) {
            Text(
                text = uiState.confirmPasswordError,
                color = colorResource(id = R.color.error),
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }


    Spacer(modifier = Modifier.height(30.dp.responsive(30.dp, 35.dp, 40.dp, deviceSize)))


// Register Button (validasyon ViewModel'de)
    AuthButton(
        onClick = onRegister,
        colorRes = R.color.primary_purple,
        borderColor = R.color.border_transparent,
        label = if (isLoading) "Kaydediliyor..." else "Kayıt Ol",
        deviceSize = deviceSize,
        isLoading = isLoading
    )


    Spacer(modifier = Modifier.height(20.dp.responsive(20.dp, 24.dp, 28.dp, deviceSize)))
    // Login Navigation
    TextButton(
        onClick = onNavigateToLogin,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading
    ) {
        Text(
            text = "Zaten hesabın var mı? Giriş Yap",
            color = colorResource(id = R.color.text_primary),
            fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
        )
    }
}
}
@Composable
private fun HandleUIState(
    uiState: Resource<*>?,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    onConsumed: () -> Unit
) {
// Tekrarlı tetiklenmeyi önlemek için uiState değişimine bağla
    LaunchedEffect(uiState) {
        when (uiState) {
            is Resource.Success -> {
                snackbarHostState.showSnackbar("Kayıt Başarılı")
                onConsumed()
                navController.navigate("login_page") {
                    popUpTo("register_page") { inclusive = true }
                }
            }
            is Resource.Error -> {
                val errorMessage = uiState.message ?: "Bir hata oluştu"
                snackbarHostState.showSnackbar(errorMessage)
                onConsumed()
            }
            else -> Unit
        }
    }
}
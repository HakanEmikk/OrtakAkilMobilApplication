package com.hakanemik.ortakakil.ui.page

import LoginButton
import LoginTextFields
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.ui.util.DeviceSize
import com.hakanemik.ortakakil.ui.util.currentDeviceSizeHelper
import com.hakanemik.ortakakil.ui.util.responsive
import com.hakanemik.ortakakil.ui.util.responsiveSp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(navController: NavController, snackbarHostState: SnackbarHostState) {
    val deviceSize = currentDeviceSizeHelper()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {  innerPadding ->

        when (deviceSize) {
            DeviceSize.Compact -> {
                // Telefon - Dikey layout
                CompactRegisterLayout(deviceSize, navController)
            }
            DeviceSize.Medium -> {
                // Tablet - Ortalanmış layout
                MediumRegisterLayout(deviceSize, navController)
            }
            DeviceSize.Expanded -> {
                // Masaüstü - Yatay split layout
                ExpandedRegisterLayout(deviceSize, navController)
            }
        }
    }
}

@Composable
private fun CompactRegisterLayout(
    deviceSize: DeviceSize,
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
        RegisterContent(deviceSize, navController)
    }
}

@Composable
private fun MediumRegisterLayout(
    deviceSize: DeviceSize,
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
            RegisterContent(deviceSize, navController)
        }

        // Sağ taraf - boş alan
        Spacer(modifier = Modifier.weight(0.15f))
    }
}

@Composable
private fun ExpandedRegisterLayout(
    deviceSize: DeviceSize,
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
            RegisterFormOnly(deviceSize, navController)
        }
    }
}

@Composable
fun RegisterContent(
    deviceSize: DeviceSize,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
        deviceSize = deviceSize,
        onNameChange = { name = it },
        onSurnameChange = { surname = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onRegister = { /* TODO: Register logic */ },
        onNavigateToLogin = { navController.navigate("login_page") }
    )

    Spacer(modifier = Modifier.height(50.dp.responsive(50.dp, 60.dp, 40.dp, deviceSize)))
}

@Composable
private fun RegisterFormOnly(
    deviceSize: DeviceSize,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


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
        deviceSize = deviceSize,
        onNameChange = { name = it },
        onSurnameChange = { surname = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onRegister = { /* TODO: Register logic */ },
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
    deviceSize: DeviceSize,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Name TextField
    LoginTextFields(
        onValueChange = onNameChange,
        label = "İsim",
        value = name,
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Surname TextField
    LoginTextFields(
        onValueChange = onSurnameChange,
        label = "Soyisim",
        value = surname,
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Email TextField
    LoginTextFields(
        onValueChange = onEmailChange,
        label = "E-mail",
        value = email,
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Password TextField
    LoginTextFields(
        onValueChange = onPasswordChange,
        label = "Şifre",
        value = password,
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 18.dp, 20.dp, deviceSize)))

    // Confirm Password TextField
    LoginTextFields(
        onValueChange = onConfirmPasswordChange,
        label = "Şifre Tekrar",
        value = confirmPassword,
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(30.dp.responsive(30.dp, 35.dp, 40.dp, deviceSize)))

    // Register Button
    LoginButton(
        onClick = onRegister,
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
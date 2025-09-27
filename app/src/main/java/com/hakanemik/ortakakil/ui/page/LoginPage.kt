import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.hakanemik.ortakakil.entity.LoginRequest
import com.hakanemik.ortakakil.entity.Resource
import com.hakanemik.ortakakil.ui.util.DeviceSize
import com.hakanemik.ortakakil.ui.util.currentDeviceSizeHelper
import com.hakanemik.ortakakil.ui.util.responsive
import com.hakanemik.ortakakil.ui.util.responsiveSp
import com.hakanemik.ortakakil.viewmodel.LoginPageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginPage(navController: NavController, snackbarHostState: SnackbarHostState) {
    val viewModel: LoginPageViewModel = viewModel()
    val uiState by viewModel.uiState.observeAsState()
    val deviceSize = currentDeviceSizeHelper()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        when (deviceSize) {
            DeviceSize.Compact -> {
                // Telefon - Dikey layout
                CompactLoginLayout(deviceSize, viewModel, navController)
            }
            DeviceSize.Medium -> {
                // Tablet - Ortalanmış layout
                MediumLoginLayout(deviceSize, viewModel, navController)
            }
            DeviceSize.Expanded -> {
                // Masaüstü - Yatay split layout
                ExpandedLoginLayout(deviceSize, viewModel, navController)
            }
        }

        // Snackbar ve navigation handling
        HandleUIState(uiState, snackbarHostState, navController)
    }
}

@Composable
private fun CompactLoginLayout(
    deviceSize: DeviceSize,
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
        LoginContent(deviceSize, viewModel, navController)
    }
}

@Composable
private fun MediumLoginLayout(
    deviceSize: DeviceSize,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark))
    ) {
        // Sol taraf - boş alan
        Spacer(modifier = Modifier.weight(0.2f))

        // Orta - Login formu
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginContent(deviceSize, viewModel, navController)
        }

        // Sağ taraf - boş alan
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
private fun ExpandedLoginLayout(
    deviceSize: DeviceSize,
    viewModel: LoginPageViewModel,
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
        }

        // Sağ taraf - Login formu
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(48.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginFormOnly(deviceSize, viewModel, navController)
        }
    }
}

@Composable
fun LoginContent(
    deviceSize: DeviceSize,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

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
        email = email,
        password = password,
        rememberMe = rememberMe,
        emailError = emailError,
        passwordError = passwordError,
        deviceSize = deviceSize,
        onEmailChange = {
            email = it
            emailError = null
        },
        onPasswordChange = {
            password = it
            passwordError = null
        },
        onRememberMeChange = { rememberMe = it },
        onLogin = { emailErr, passErr ->
            emailError = emailErr
            passwordError = passErr
            if (emailErr == null && passErr == null) {
                val loginRequest = LoginRequest(email = email, password = password)
                viewModel.login(loginRequest)
            }
        },
        onNavigateToRegister = { navController.navigate("register_page") },
        isLoading = viewModel.uiState.value is Resource.Loading
    )

    Spacer(modifier = Modifier.height(50.dp.responsive(50.dp, 60.dp, 40.dp, deviceSize)))
}

@Composable
private fun LoginFormOnly(
    deviceSize: DeviceSize,
    viewModel: LoginPageViewModel,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Text(
        text = "Giriş Yap",
        fontSize = 32.sp,
        color = colorResource(id = R.color.text_primary),
        modifier = Modifier.padding(bottom = 32.dp)
    )

    LoginForm(
        email = email,
        password = password,
        rememberMe = rememberMe,
        emailError = emailError,
        passwordError = passwordError,
        deviceSize = deviceSize,
        onEmailChange = {
            email = it
            emailError = null
        },
        onPasswordChange = {
            password = it
            passwordError = null
        },
        onRememberMeChange = { rememberMe = it },
        onLogin = { emailErr, passErr ->
            emailError = emailErr
            passwordError = passErr
            if (emailErr == null && passErr == null) {
                val loginRequest = LoginRequest(email = email, password = password)
                viewModel.login(loginRequest)
            }
        },
        onNavigateToRegister = { navController.navigate("register_page") },
        isLoading = viewModel.uiState.value is Resource.Loading
    )
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    rememberMe: Boolean,
    emailError: String?,
    passwordError: String?,
    deviceSize: DeviceSize,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: (String?, String?) -> Unit,
    onNavigateToRegister: () -> Unit,
    isLoading: Boolean
) {
    // Email TextField
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

    Spacer(modifier = Modifier.height(21.dp.responsive(21.dp, 24.dp, 28.dp, deviceSize)))

    // Password TextField
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

    Spacer(modifier = Modifier.height(15.dp.responsive(15.dp, 20.dp, 24.dp, deviceSize)))

    // Checkbox Row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMe,
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

    // Login Button
    LoginButton(
        onClick = {
            var emailErr: String? = null
            var passErr: String? = null

            if (email.isBlank()) {
                emailErr = "E-posta boş olamaz"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailErr = "Geçerli bir e-posta giriniz"
            }

            if (password.isBlank()) {
                passErr = "Şifre boş olamaz"
            } else if (password.length < 6) {
                passErr = "Şifre en az 6 karakter olmalıdır"
            }

            onLogin(emailErr, passErr)
        },
        colorRes = R.color.primary_purple,
        isLoading = isLoading,
        borderColor = R.color.border_transparent,
        label = "Giriş Yap",
        deviceSize = deviceSize
    )

    Spacer(modifier = Modifier.height(17.dp.responsive(17.dp, 20.dp, 24.dp, deviceSize)))

    // Register Button
    LoginButton(
        onClick = onNavigateToRegister,
        colorRes = R.color.transparent,
        borderColor = R.color.border_default,
        label = "Kayıt Ol",
        deviceSize = deviceSize
    )
}

@Composable
fun LoginTextFields(
    onValueChange: (String) -> Unit,
    label: String,
    value: String,
    deviceSize: DeviceSize
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp.responsive(60.dp, 65.dp, 70.dp, deviceSize))
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.surface_light),
            unfocusedContainerColor = colorResource(id = R.color.surface_dark),
            disabledContainerColor = colorResource(id = R.color.surface_dark),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLabelColor = colorResource(id = R.color.surface_light),
            cursorColor = colorResource(id = R.color.white),
            focusedTextColor = colorResource(id = R.color.white),
            unfocusedTextColor = colorResource(id = R.color.white),
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    colorRes: Int,
    borderColor: Int = colorRes,
    label: String,
    textColor: Color = colorResource(id = R.color.text_primary),
    isLoading: Boolean = false,
    deviceSize: DeviceSize
) {
    Button(
        enabled = !isLoading,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp.responsive(60.dp, 65.dp, 70.dp, deviceSize))
            .clip(RoundedCornerShape(10.dp))
            .border(
                2.dp,
                color = colorResource(id = borderColor),
                shape = RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = colorRes)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.white),
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = label,
                color = textColor,
                fontSize = 16f.responsiveSp(16f, 18f, 20f, deviceSize)
            )
        }
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
                snackbarHostState.showSnackbar("Giriş başarılı")
            }
            navController.navigate("home_page") {
                popUpTo("login_page") { inclusive = true }
            }
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
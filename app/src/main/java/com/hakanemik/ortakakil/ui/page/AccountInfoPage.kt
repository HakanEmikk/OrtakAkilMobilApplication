package com.hakanemik.ortakakil.ui.page

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.FileHelper
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.ui.utils.ProfileOptionSheet
import com.hakanemik.ortakakil.viewmodel.AccountInfoPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInfoPage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: AccountInfoPageViewModel = hiltViewModel()
) {
    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AccountInfoPageViewModel.UiEffect.ShowSnackbar -> {
                    onShowSnackbar(event.message, event.type)
                }
            }
        }
    }

    // --- Launchers ---
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> viewModel.onImageSelected(uri) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) viewModel.onImageSelected(tempUri) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            FileHelper.createImageUri(context).let { uri ->
                tempUri = uri
                cameraLauncher.launch(uri)
            }
        } else {
            onShowSnackbar("Kamera izni reddedildi.", SnackbarType.WARNING)
        }
    }

    val imageSource = uiState.photoUri ?: uiState.photoUrl.takeIf { !it.isNullOrEmpty() } ?: R.drawable.user

    Box(modifier = Modifier
        .fillMaxSize().navigationBarsPadding()
        .background(colorResource(id = R.color.background_dark))
        .pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            // --- Profile Image (Premium Edit Style) ---
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .border(
                            2.dp,
                            brush = Brush.linearGradient(
                                listOf(colorResource(R.color.gradient_start), colorResource(R.color.gradient_end))
                            ),
                            CircleShape
                        )
                        .padding(6.dp)
                ) {
                    AsyncImage(
                        model = imageSource,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        error = painterResource(R.drawable.user),
                        fallback = painterResource(R.drawable.user)
                    )
                }

                // Edit Button Icon
                IconButton(
                    onClick = { showSheet = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .size(36.dp)
                        .background(colorResource(R.color.primary_purple), CircleShape)
                        .border(2.dp, colorResource(R.color.background_dark), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pencil),
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- Editable Fields (Glassmorphism) ---
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    EditableFieldCard(
                        label = "Ad",
                        value = uiState.firstName,
                        onValueChange = viewModel::updateFirstName,
                        deviceSize = deviceSize,
                        error = uiState.firstNameError
                    )
                }
                item {
                    EditableFieldCard(
                        label = "Soyad",
                        value = uiState.lastName,
                        onValueChange = viewModel::updateLastName,
                        deviceSize = deviceSize,
                        error = uiState.lastNameError
                    )
                }
                item {
                    EditableFieldCard(
                        label = "E-posta",
                        value = uiState.email,
                        onValueChange = viewModel::updateEmail,
                        deviceSize = deviceSize,
                        enabled = uiState.authProvider != "google",
                        error = uiState.emailError
                    )
                }
            }

            // --- Action Button ---
            Button(
                onClick = viewModel::updateProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary_purple)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    "Değişiklikleri Kaydet",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showSheet) {
        ProfileOptionSheet(
            onDismiss = { showSheet = false },
            onCameraClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
            onGalleryClick = {
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
    }
}

@Composable
fun EditableFieldCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    deviceSize: DeviceSize,
    enabled: Boolean = true,
    error: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = colorResource(id = R.color.primary_purple),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.surface_light).copy(alpha = 0.3f)
            ),
            border = if (enabled) BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)) else null
        ) {
            TextField(
                enabled = enabled,
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = colorResource(id = R.color.primary_purple)
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        if (error != null) {
            Text(
                text = error,
                color = colorResource(id = R.color.error),
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
@file:Suppress("UNUSED_EXPRESSION")

package com.hakanemik.ortakakil.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.viewmodel.AccountInfoPageViewModel

@Composable
fun AccountInfoPage(
    navController: NavController,
    viewModel: AccountInfoPageViewModel = hiltViewModel()
) {
    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    colorResource(id = R.color.background_dark),
                    colorResource(id = R.color.background_dark).copy(alpha = 0.95f),
                    colorResource(id = R.color.surface_dark).copy(alpha = 0.4f)
                )
            )
        )
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    focusManager.clearFocus(force = true)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
                            )
                        ),
                        shape = CircleShape
                    )
                    .padding(1.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    contentDescription = "Profile Image",
                    painter = painterResource(id = R.drawable.ortak_akil_logo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(38.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pencil),
                        contentDescription = "Edit Image",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Editable Fields
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    EditableFieldCard(
                        label = "Ad",
                        value = uiState.firstName,
                        onValueChange = viewModel::updateFirstName,
                        deviceSize = deviceSize,
                    )
                }

                item {
                    EditableFieldCard(
                        label = "Soyad",
                        value = uiState.lastName,
                        onValueChange = viewModel::updateLastName,
                        deviceSize = deviceSize,
                    )
                }

                item {
                    EditableFieldCard(
                        label = "E-posta",
                        value = uiState.email,
                        onValueChange = viewModel::updateEmail,
                        deviceSize = deviceSize,
                        enabled = uiState.authProvider != "google"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Delete Account Button
            Button(
                onClick = viewModel::updateProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.error)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    "Hesabı Sil",
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 16f.responsiveSp(16f, 18f, 20f, deviceSize),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun EditableFieldCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    deviceSize: DeviceSize,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = colorResource(id = R.color.text_secondary),
            fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.surface_light)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                    TextField(
                        enabled = enabled,
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        modifier = Modifier
                            .height(56.dp).fillMaxSize(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = colorResource(id = R.color.text_primary),
                            unfocusedTextColor = colorResource(id = R.color.text_primary),
                            disabledTextColor = colorResource(id = R.color.text_primary) ,
                            cursorColor = colorResource(id = R.color.primary_purple),

                        )
                    )


                }
            }
        }
    }



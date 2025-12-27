package com.hakanemik.ortakakil.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.viewmodel.ProfilePageViewModel

@Composable
fun ProfilePage(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ProfilePageViewModel = hiltViewModel()
) {

    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageSource=if (uiState.photoUrl == "")  R.drawable.person else uiState.photoUrl
    Column(
        modifier = Modifier
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
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp.responsive(32.dp, 40.dp, 36.dp, deviceSize)))

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
            AsyncImage(
                contentDescription = "Profile Image",
                model =  imageSource,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(28.dp.responsive(28.dp, 32.dp, 28.dp, deviceSize)))

        // User Name
        Text(
            text = uiState.userName.uppercase(),
            color = colorResource(id = R.color.text_primary),
            fontSize = 32f.responsiveSp(32f, 36f, 40f, deviceSize),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        // User Email
        Text(
            text = uiState.email,
            color = colorResource(id = R.color.text_secondary),
            fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize),
        )

        Spacer(Modifier.height(32.dp.responsive(32.dp, 40.dp, 36.dp, deviceSize)))

        // Profile Stats Section
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 220.dp)
        ) {
            item { ProfilInfoCard(number = uiState.totalDecisionCount, numberText = "Soru Soruldu", deviceSize) }
            item { ProfilInfoCard(number = uiState.totalShareCount, numberText = "Cevap Paylaşıldı", deviceSize) }
        }

        Spacer(Modifier.height(40.dp.responsive(40.dp, 44.dp, 40.dp, deviceSize)))

        // Settings Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Ayarlar",
                color = colorResource(id = R.color.text_primary),
                fontSize = 18f.responsiveSp(18f, 20f, 22f, deviceSize),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        // Settings Cards
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 350.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                SettingsCard(
                    settingName = "Hesap Bilgileri",
                    leftIcon = R.drawable.person,
                    onClick = {navController.navigate("account_info_page")},
                    deviceSize
                )
            }
            item {
                SettingsCard(
                    settingName = "Bildirim Ayarları",
                    leftIcon = R.drawable.notification,
                    onClick = {navController.navigate("notification_settings_page")},
                    deviceSize
                )
            }
            item {
                SettingsCard(
                    settingName = "Gizlilik ve Güvenlik",
                    leftIcon = R.drawable.handshake,
                    onClick = {},
                    deviceSize
                )
            }
            item {
                SettingsCard(
                    settingName = "Yardım ve Destek",
                    leftIcon = R.drawable.question,
                    onClick = {},
                    deviceSize
                )
            }
        }

        Spacer(Modifier.height(32.dp.responsive(32.dp, 40.dp, 36.dp, deviceSize)))
    }
}

@Composable
fun ProfilInfoCard(
    number: String,
    numberText: String,
    deviceSize: DeviceSize
) {
    Card(
        modifier = Modifier.aspectRatio(2.1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_light)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                colors = listOf(
                    colorResource(id = R.color.purple_overlay_20),
                    colorResource(id = R.color.purple_overlay_10)
                )
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                color = colorResource(R.color.primary_purple),
                fontSize = 28f.responsiveSp(28f, 32f, 36f, deviceSize),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = numberText,
                color = colorResource(id = R.color.text_secondary),
                fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SettingsCard(
    settingName: String,
    leftIcon: Int,
    onClick: () -> Unit,
    deviceSize: DeviceSize
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp.responsive(56.dp, 60.dp, 56.dp, deviceSize))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_light)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp.responsive(16.dp, 18.dp, 16.dp, deviceSize)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colorResource(id = R.color.purple_overlay_10)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = leftIcon),
                        contentDescription = "settings",
                        tint = colorResource(id = R.color.primary_purple),
                        modifier = Modifier.size(20.dp.responsive(20.dp, 24.dp, 20.dp, deviceSize))
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    settingName,
                    color = colorResource(id = R.color.text_primary),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15f.responsiveSp(15f, 16f, 17f, deviceSize)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "arrow",
                tint = colorResource(id = R.color.text_secondary),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
package com.hakanemik.ortakakil.ui.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.viewmodel.ProfilePageViewModel

@Composable
fun ProfilePage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: ProfilePageViewModel = hiltViewModel()
) {
    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageSource = if (uiState.photoUrl.isEmpty()) R.drawable.user else uiState.photoUrl

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp.responsive(48.dp, 56.dp, 64.dp, deviceSize)))

        // --- 1. PROFIL FOTOĞRAFI (Premium Glow Border) ---
        Box(
            modifier = Modifier
                .size(130.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.gradient_start),
                            colorResource(id = R.color.gradient_end)
                        )
                    ),
                    shape = CircleShape
                )
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageSource,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                error = painterResource(R.drawable.user),
                fallback = painterResource(R.drawable.user)
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- 2. KULLANICI BİLGİLERİ ---
        Text(
            text = uiState.userName.lowercase().replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = uiState.email,
            color = colorResource(id = R.color.text_muted),
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(Modifier.height(32.dp))

        // --- 3. İSTATİSTİK KARTLARI (Glassmorphism) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfilInfoCard(
                modifier = Modifier.weight(1f),
                number = uiState.totalDecisionCount,
                label = "Soru Soruldu",
                deviceSize = deviceSize
            )
            ProfilInfoCard(
                modifier = Modifier.weight(1f),
                number = uiState.totalShareCount,
                label = "Paylaşım",
                deviceSize = deviceSize
            )
        }

        Spacer(Modifier.height(40.dp))

        // --- 4. AYARLAR LİSTESİ ---
        Text(
            "Hesap Ayarları",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingsCard("Hesap Bilgileri", R.drawable.person, { navController.navigate("account_info_page") }, deviceSize)
            SettingsCard("Bildirim Ayarları", R.drawable.notification, { navController.navigate("notification_settings_page") }, deviceSize)
            SettingsCard("Engellenen Kullanıcılar", R.drawable.block_person, { navController.navigate("blocked_users_page") }, deviceSize)
            SettingsCard("Gizlilik ve Güvenlik", R.drawable.handshake, {}, deviceSize)

            // Çıkış Yap (Opsiyonel ama şık durur)
//            SettingsCard("Çıkış Yap", R.drawable.settings, {}, deviceSize, isDanger = true)
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun ProfilInfoCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    deviceSize: DeviceSize
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_dark).copy(alpha = 0.5f)
        ),
        border = BorderStroke(
            1.dp,
            brush = Brush.verticalGradient(listOf(Color.White.copy(0.1f), Color.Transparent))
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                color = colorResource(R.color.primary_purple),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                color = colorResource(id = R.color.text_muted),
                fontSize = 12.sp,
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
    deviceSize: DeviceSize,
    isDanger: Boolean = false
) {
    val contentColor = if (isDanger) colorResource(R.color.warning) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_light).copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isDanger) colorResource(R.color.warning).copy(0.1f)
                        else colorResource(id = R.color.primary_purple).copy(0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = leftIcon),
                    contentDescription = null,
                    tint = if (isDanger) colorResource(R.color.warning) else colorResource(id = R.color.primary_purple),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = settingName,
                color = contentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = null,
                tint = colorResource(id = R.color.text_muted),
                modifier = Modifier.size(16.dp).graphicsLayer(rotationZ = 180f)
            )
        }
    }
}
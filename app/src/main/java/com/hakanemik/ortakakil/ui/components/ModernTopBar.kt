package com.hakanemik.ortakakil.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.TopBarState
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.responsive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(topBarState: TopBarState, deviceSize: DeviceSize) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(), // Sistemin üst barı (saat/pil) ile çakışmaması için
        navigationIcon = {
            if (topBarState.isHomePage) {
                // Ana Sayfa Profil Görünümü
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(42.dp)
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                listOf(colorResource(R.color.gradient_start), colorResource(R.color.gradient_end))
                            ),
                            shape = CircleShape
                        )
                        .padding(2.dp)
                ) {
                    AsyncImage(
                        model = if (topBarState.userPictureUrl.isNullOrEmpty()) R.drawable.user else topBarState.userPictureUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable { topBarState.onLeftIconClick() },
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.user),
                        fallback = painterResource(R.drawable.user)
                    )
                }
            } else if (topBarState.leftIcon != null) {
                // Diğer Sayfalar Geri Butonu
                IconButton(onClick = topBarState.onLeftIconClick) {
                    Icon(
                        painter = painterResource(id = topBarState.leftIcon),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp.responsive(24.dp, 32.dp, 24.dp, deviceSize))
                    )
                }
            }
        },
        title = {
            if (topBarState.isHomePage) {
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = "Merhaba 👋",
                        fontSize = 12.sp,
                        color = colorResource(R.color.text_muted),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = topBarState.userName?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Misafir",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            } else {
                // Sayfa Başlığı (Merkezi yerleşim için Box/Row yapısı)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = topBarState.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        // Sağda ikon yoksa tam ortalamak için sağ padding ekliyoruz
                        modifier = Modifier.padding(end = if (topBarState.rightIcon == null) 48.dp else 0.dp)
                    )
                }
            }
        },
        actions = {
            if (topBarState.rightIcon != null) {
                IconButton(onClick = topBarState.onRightIconClick) {
                    Icon(
                        painter = painterResource(id = topBarState.rightIcon),
                        contentDescription = "Right Action",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp.responsive(24.dp, 32.dp, 24.dp, deviceSize))
                    )
                }
            } else if (topBarState.isHomePage) {
                // Ana sayfada sağ tarafa boşluk bırakmak tasarım dengesi için iyidir
                Spacer(modifier = Modifier.width(16.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.background_dark).copy(alpha = 0.9f),
            scrolledContainerColor = colorResource(id = R.color.background_dark)
        )
    )
}
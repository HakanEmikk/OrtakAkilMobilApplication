package com.hakanemik.ortakakil.ui.page

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive

data class NotificationItem(
    val title: String,
    val description: String,
    val iconResId: Int,
    var isEnabled: Boolean = true
)

@Composable
fun NotificationSettingsPage() {
    val deviceSize = currentDeviceSizeHelper()

    // Liste yapısını daha sade ve yönetilebilir hale getirdik
    val notificationItems = remember {
        mutableStateListOf(
            NotificationItem(
                title = "Genel Bildirimler",
                description = "Tüm bildirim türlerini yönet",
                iconResId = R.drawable.notification,
                isEnabled = true
            ),
            NotificationItem(
                title = "Sosyal Bildirimler",
                description = "Etkileşimler ve yeni paylaşımlar",
                iconResId = R.drawable.world, // world ikonunu kullandım sosyal için
                isEnabled = false
            ),
            NotificationItem(
                title = "Gelişmeler",
                description = "Uygulama haberleri ve yenilikler",
                iconResId = R.drawable.pencil,
                isEnabled = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(32.dp.responsive(32.dp, 40.dp, 36.dp, deviceSize)))

        Text(
            text = "Bildirim Tercihleri",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Hangi durumlarda haberdar olmak istediğini seçebilirsin.",
            color = colorResource(id = R.color.text_muted),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Notification Items
        notificationItems.forEach { item ->
            NotificationCard(
                item = item,
                onToggle = { newValue ->
                    // ViewModel bağlantısı yapıldığında burası güncellenir
                    item.isEnabled = newValue
                },
                deviceSize = deviceSize
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun NotificationCard(
    item: NotificationItem,
    onToggle: (Boolean) -> Unit,
    deviceSize: DeviceSize
) {
    var isChecked by remember { mutableStateOf(item.isEnabled) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_light).copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            1.dp,
            brush = Brush.verticalGradient(listOf(Color.White.copy(0.1f), Color.Transparent))
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sol İkon Alanı
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(id = R.color.primary_purple).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.iconResId),
                    contentDescription = null,
                    tint = colorResource(id = R.color.primary_purple),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Metin İçeriği
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.description,
                    color = colorResource(id = R.color.text_muted),
                    fontSize = 12.sp
                )
            }

            // Switch (Modernize edilmiş renkler)
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onToggle(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = colorResource(id = R.color.primary_purple),
                    uncheckedThumbColor = colorResource(id = R.color.text_muted),
                    uncheckedTrackColor = colorResource(id = R.color.surface_dark).copy(alpha = 0.5f),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}
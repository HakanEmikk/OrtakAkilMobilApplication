package com.hakanemik.ortakakil.ui.page

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
    val gradientColors: List<String>,
    var isEnabled: Boolean = true
)

@Composable
fun NotificationSettingsPage() {
    val deviceSize = currentDeviceSizeHelper()

    val notificationItems = remember {
        mutableListOf(
            NotificationItem(
                title = "Genel Bildirimler",
                description = "Tüm bildirim türlerini aç veya kapat",
                iconResId = R.drawable.notification,
                gradientColors = listOf("#22C55E", "#16A34A"),
                isEnabled = true
            ),
            NotificationItem(
                title = "Sosyal Bildirimler",
                description = "Etkileşimler hakkında",
                iconResId = R.drawable.handshake,
                gradientColors = listOf("#A855F7", "#7C3AED"),
                isEnabled = false
            ),
            NotificationItem(
                title = "Promosyon ve Haberler",
                description = "Yenilikler ve teklifler",
                iconResId = R.drawable.question,
                gradientColors = listOf("#F97316", "#D97706"),
                isEnabled = true
            )
        )
    }

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
            .padding(top = 20.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(32.dp.responsive(32.dp, 36.dp, 32.dp, deviceSize)))

        // Notification Items
        notificationItems.forEachIndexed { index, item ->
            var isEnabled by remember { mutableStateOf(item.isEnabled) }

            NotificationCard(
                item = item,
                isEnabled = isEnabled,
                onToggle = {
                    isEnabled = it
                    item.isEnabled = it
                },
                deviceSize = deviceSize
            )

            if (index < notificationItems.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun NotificationCard(
    item: NotificationItem,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    deviceSize: DeviceSize
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_light)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon Box with Gradient Background
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                parseColor(item.gradientColors[0]),
                                parseColor(item.gradientColors[1])
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.iconResId),
                    contentDescription = item.title,
                    tint = colorResource(id = R.color.text_primary),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            // Text Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.description,
                    color = colorResource(id = R.color.text_secondary),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Toggle Switch
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                modifier = Modifier.size(width = 48.dp, height = 28.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.text_primary),
                    checkedTrackColor = colorResource(id = R.color.primary_purple),
                    uncheckedThumbColor = colorResource(id = R.color.text_muted),
                    uncheckedTrackColor = colorResource(id = R.color.surface_dark)
                )
            )
        }
    }
}

@Composable
fun parseColor(hexColor: String): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(Color.parseColor(hexColor))
}
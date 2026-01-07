package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOptionSheet(
    onDismiss: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorResource(id = R.color.surface_dark),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray.copy(alpha = 0.5f)) },
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp, start = 24.dp, end = 24.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profil Fotoğrafı",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "Görünümünü güncellemek için bir yöntem seç",
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_muted),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Kamera Seçeneği
                OptionItem(
                    modifier = Modifier.weight(1f),
                    title = "Kamera",
                    subtitle = "Yeni bir fotoğraf çek",
                    iconRes = R.drawable.work, // Buraya kamera ikonu (R.drawable.camera) koyabilirsin
                    onClick = {
                        onCameraClick()
                        onDismiss()
                    }
                )

                // Galeri Seçeneği
                OptionItem(
                    modifier = Modifier.weight(1f),
                    title = "Galeri",
                    subtitle = "Albümden seçim yap",
                    iconRes = R.drawable.shopping_cart, // Buraya galeri ikonu (R.drawable.gallery) koyabilirsin
                    onClick = {
                        onGalleryClick()
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
fun OptionItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.surface_light).copy(alpha = 0.3f))
            .clickable { onClick() }
            .padding(vertical = 24.dp)
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = colorResource(id = R.color.primary_purple).copy(alpha = 0.15f)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.padding(16.dp),
                tint = colorResource(id = R.color.primary_purple)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = subtitle,
            color = colorResource(id = R.color.text_muted),
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
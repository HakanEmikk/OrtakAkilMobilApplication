package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerShareSheet(
    onDismiss: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorResource(id = R.color.surface_dark),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray.copy(alpha = 0.5f)) },
        scrimColor = Color.Black.copy(alpha = 0.5f),
        sheetState = sheetState,
        windowInsets = WindowInsets.ime,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
                .imePadding(), // Klavye açıldığında içeriği yukarı kaydırır
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- ICON SECTION (Daha Parlak ve Estetik) ---
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        colorResource(id = R.color.primary_purple).copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.world),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = colorResource(id = R.color.primary_purple)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- TITLE & SUBTITLE ---
            Text(
                text = "Keşfet'te Paylaş",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Analizini toplulukla paylaşarak farklı görüşler alabilir ve diğerlerine ilham verebilirsin.",
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_muted),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- INPUT SECTION ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Düşüncelerini Ekle",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primary_purple),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.surface_light).copy(alpha = 0.4f)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            listOf(Color.White.copy(0.1f), Color.Transparent)
                        )
                    )
                ) {
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        placeholder = {
                            Text(
                                text = "Neden bu soruyu sordun? Paylaşırken bir not ekle...",
                                color = colorResource(id = R.color.text_muted),
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = colorResource(id = R.color.primary_purple),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- ACTION BUTTON ---
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary_purple)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Şimdi Paylaş",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
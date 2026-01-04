package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerShareSheet(
    onDismiss : () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit

){
    ModalBottomSheet(
        onDismissRequest = onDismiss ,
        containerColor = colorResource(id = R.color.surface_dark),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) },
        scrimColor = Color.Black.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon Section
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = colorResource(id = R.color.primary_purple).copy(alpha = 0.15f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.emoji_people),
                    contentDescription = "",
                    modifier = Modifier.padding(16.dp),
                    tint = colorResource(id = R.color.primary_purple)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title Section
            Text(
                "Paylaş",
                color = colorResource(id = R.color.text_primary),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Sorunuz keşfet akışında görünecek.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.text_secondary),
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Content Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Not Ekle",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.text_primary)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(14.dp),
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
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        placeholder = {
                            Text(
                                text = "Sorunu buraya yaz...",
                                color = colorResource(id = R.color.text_muted),
                                fontSize = 14.sp
                            )
                        },
                        singleLine = false,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = colorResource(id = R.color.primary_purple),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = colorResource(id = R.color.text_primary),
                            unfocusedTextColor = colorResource(id = R.color.text_primary),
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Share Button
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primary_purple)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.world),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(id = R.color.text_primary)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        "Paylaş",
                        color = colorResource(id = R.color.text_primary),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
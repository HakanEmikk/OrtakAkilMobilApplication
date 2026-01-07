package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.helper.DeviceSize

@Composable
fun AuthButton(
    onClick: () -> Unit,
    colorRes: Int,
    borderColor: Int = colorRes,
    label: String,
    isLoading: Boolean = false,
    deviceSize: DeviceSize
) {
    Button(
        enabled = !isLoading,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = colorRes),
            disabledContainerColor = colorResource(id = R.color.surface_light)
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (colorRes == R.color.transparent) BorderStroke(1.dp, Color.White.copy(0.1f)) else null
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
        } else {
            Text(text = label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
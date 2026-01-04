package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp

@Composable
fun AuthButton(
    onClick: () -> Unit,
    colorRes: Int,
    borderColor: Int = colorRes,
    label: String,
    textColor: Color = colorResource(id = R.color.text_primary),
    isLoading: Boolean = false,
    deviceSize: DeviceSize
) {
    Button(
        enabled = !isLoading,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp.responsive(52.dp, 56.dp, 60.dp, deviceSize))
            .clip(RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = colorRes),
            disabledContainerColor = colorResource(id = R.color.surface_light)
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = 2.dp,
            color = colorResource(id = borderColor)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.white),
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = label,
                color = textColor,
                fontSize = 16f.responsiveSp(16f, 18f, 20f, deviceSize)
            )
        }
    }
}
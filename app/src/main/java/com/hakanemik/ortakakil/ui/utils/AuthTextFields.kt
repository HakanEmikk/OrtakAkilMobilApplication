package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp

@Composable
fun AuthTextFields(
    onValueChange: (String) -> Unit,
    label: String,
    value: String,
    deviceSize: DeviceSize,
    isPassword: Boolean = false
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp.responsive(56.dp, 60.dp, 56.dp, deviceSize))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colorResource(id = R.color.gradient_start).copy(alpha = 0.3f),
                            colorResource(id = R.color.gradient_end).copy(alpha = 0.3f)
                        )
                    ), shape = RoundedCornerShape(14.dp)
                ),
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    color = colorResource(id = R.color.text_secondary),
                    fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
                )
                    },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .clip(RoundedCornerShape(13.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.surface_dark),
                unfocusedContainerColor = colorResource(id = R.color.surface_light),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = colorResource(id = R.color.primary_purple),
                unfocusedLabelColor = colorResource(id = R.color.text_secondary),
                cursorColor = colorResource(id = R.color.primary_purple),
                focusedTextColor = colorResource(id = R.color.text_primary),
                unfocusedTextColor = colorResource(id = R.color.text_primary),
                ),
            shape = RoundedCornerShape(13.dp),
            )
    }
}
package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
    deviceSize: DeviceSize
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14f.responsiveSp(14f, 16f, 18f, deviceSize)
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp.responsive(60.dp, 65.dp, 70.dp, deviceSize))
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.surface_light),
            unfocusedContainerColor = colorResource(id = R.color.surface_dark),
            disabledContainerColor = colorResource(id = R.color.surface_dark),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLabelColor = colorResource(id = R.color.surface_light),
            cursorColor = colorResource(id = R.color.white),
            focusedTextColor = colorResource(id = R.color.white),
            unfocusedTextColor = colorResource(id = R.color.white),

            ),
        shape = RoundedCornerShape(10.dp),
        )
}
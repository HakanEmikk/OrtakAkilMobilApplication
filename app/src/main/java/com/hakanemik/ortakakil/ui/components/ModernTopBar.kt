package com.hakanemik.ortakakil.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.TopBarState
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.responsive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(topBarState: TopBarState, deviceSize: DeviceSize){
    TopAppBar(
        navigationIcon = {
            if (topBarState.leftIcon != null){
                IconButton(onClick = topBarState.onLeftIconClick) {
                    Icon(
                        painter = painterResource(id = topBarState.leftIcon),
                        contentDescription = "Left Icon",
                        tint = colorResource(id = R.color.text_primary),
                        modifier = Modifier.size(24.dp.responsive(24.dp,32.dp,24.dp, deviceSize))
                    )
                }
            }
        }, title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ) { Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = topBarState.title,
                    color = colorResource(R.color.text_primary)
                )
            } }

        },
        actions = {
            if (topBarState.rightIcon != null){
                IconButton(onClick = topBarState.onRightIconClick) {
                    Icon(
                        painter = painterResource(id = topBarState.rightIcon),
                        contentDescription = "Right Icon",
                        tint = colorResource(id = R.color.text_primary),
                        modifier = Modifier.size(24.dp.responsive(24.dp, 32.dp, 24.dp, deviceSize))
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.background_dark)
        )
    )
}

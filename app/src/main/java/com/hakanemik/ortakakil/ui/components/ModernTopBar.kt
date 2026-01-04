package com.hakanemik.ortakakil.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.TopBarState
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.responsive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(topBarState: TopBarState, deviceSize: DeviceSize){
    TopAppBar(
        navigationIcon = {
            if (topBarState.leftIcon != null && !topBarState.isHomePage){
                IconButton(onClick = topBarState.onLeftIconClick) {
                    Icon(
                        painter = painterResource(id = topBarState.leftIcon),
                        contentDescription = "Left Icon",
                        tint = colorResource(id = R.color.text_primary),
                        modifier = Modifier.size(24.dp.responsive(24.dp,32.dp,24.dp, deviceSize))
                    )
                }
            }
            if (topBarState.isHomePage){
                AsyncImage(
                    model = topBarState.userPictureUrl ?: R.drawable.person, // fallback to default icon if null
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { topBarState.onLeftIconClick() },
                    contentScale = ContentScale.Crop,
                )
            }
        }, title = {
            if (topBarState.isHomePage){
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = "Merhaba 👋",
                        fontSize = 12.sp,
                        color = colorResource(R.color.text_secondary)
                    )
                    Text(
                        text = topBarState.userName?.uppercase() ?: "Misafir",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.text_primary)
                    )
                }
            }
            else{
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
            }


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

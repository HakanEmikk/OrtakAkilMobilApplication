package com.hakanemik.ortakakil.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hakanemik.ortakakil.R
import androidx.compose.ui.res.painterResource
import com.hakanemik.ortakakil.entity.NavItem

@Composable
fun ModernBottomBar(navController: NavHostController, bottomItems: List<NavItem>) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Ana konteyner
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Sistem çizgisinden kurtarır
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp) // Yandan ve ALTtan boşluk (Süzülme etkisi)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Yüksekliği sabitliyoruz
                .clip(RoundedCornerShape(24.dp)), // Dört köşeyi de oval yapıyoruz
            color = colorResource(id = R.color.surface_dark).copy(alpha = 0.95f),
            tonalElevation = 12.dp,
            shadowElevation = 8.dp // Hafif bir gölge ile derinlik katıyoruz
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomItems.forEach { item ->
                    val isSelected = currentRoute?.startsWith(item.route) == true
                    ModernNavItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ModernNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Animasyonlar
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "scale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colorResource(R.color.primary_purple).copy(alpha = 0.15f)
        else Color.Transparent,
        animationSpec = tween(300), label = "color"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            // İkon Kutusu
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.label,
                    tint = if (isSelected) colorResource(R.color.primary_purple)
                    else colorResource(R.color.text_muted),
                    modifier = Modifier
                        .size(26.dp)
                        .graphicsLayer {
                            scaleX = animatedScale
                            scaleY = animatedScale
                        }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Etiket
            Text(
                text = item.label,
                color = if (isSelected) Color.White
                else colorResource(R.color.text_muted),
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
            )

            // Seçili Göstergesi (Alt nokta)
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(4.dp)
                        .background(colorResource(R.color.primary_purple), CircleShape)
                )
            } else {
                Spacer(modifier = Modifier.size(6.dp))
            }
        }
    }
}
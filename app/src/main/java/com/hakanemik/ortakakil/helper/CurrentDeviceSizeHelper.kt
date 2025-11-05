package com.hakanemik.ortakakil.helper

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DeviceSize { Compact, Medium, Expanded }

/**
 * Cihaz boyutunu belirler
 * - Compact: Telefonlar (< 600dp) - Yönlendirme değişse de her zaman Compact
 * - Medium: Tabletler (600dp - 839dp)
 * - Expanded: Geniş tabletler ve masaüstü (≥ 840dp)
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun currentDeviceSizeHelper(): DeviceSize {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Telefon kontrolü - hızlı yol (WindowSizeClass hesabı gerektirmez)
    if (screenWidthDp < 600) {
        return DeviceSize.Compact
    }

    // Tablet ve üstü için WindowSizeClass hesapla
    return if (context is ComponentActivity) {
        val windowSizeClass = calculateWindowSizeClass(context)
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Medium -> DeviceSize.Medium
            WindowWidthSizeClass.Expanded -> DeviceSize.Expanded
            else -> DeviceSize.Compact
        }
    } else {
        // Context ComponentActivity değilse
        DeviceSize.Compact
    }
}

// Responsive Dp extension
fun Dp.responsive(
    compact: Dp,
    medium: Dp,
    expanded: Dp,
    deviceSize: DeviceSize
): Dp {
    return when(deviceSize) {
        DeviceSize.Compact -> compact
        DeviceSize.Medium -> medium
        DeviceSize.Expanded -> expanded
    }
}

// Responsive Int extension
fun Int.responsive(
    compact: Int,
    medium: Int,
    expanded: Int,
    deviceSize: DeviceSize
): Int {
    return when(deviceSize) {
        DeviceSize.Compact -> compact
        DeviceSize.Medium -> medium
        DeviceSize.Expanded -> expanded
    }
}

// Responsive Float extension
fun Float.responsive(
    compact: Float,
    medium: Float,
    expanded: Float,
    deviceSize: DeviceSize
): Float {
    return when(deviceSize) {
        DeviceSize.Compact -> compact
        DeviceSize.Medium -> medium
        DeviceSize.Expanded -> expanded
    }
}

// Responsive TextUnit extension
fun Float.responsiveSp(
    compact: Float,
    medium: Float,
    expanded: Float,
    deviceSize: DeviceSize
): TextUnit {
    return when (deviceSize) {
        DeviceSize.Compact -> compact.sp
        DeviceSize.Medium -> medium.sp
        DeviceSize.Expanded -> expanded.sp
    }
}

// Bonus: Cihaz tipini kontrol etmek için yardımcı fonksiyonlar
@Composable
fun isPhone(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp < 600
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp >= 600
}

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
}
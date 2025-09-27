package com.hakanemik.ortakakil.ui.util

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

enum class DeviceSize { Compact, Medium, Expanded }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun currentDeviceSizeHelper(): DeviceSize {
    val context = LocalContext.current
    return if (context is ComponentActivity) {
            val windowSizeClass = calculateWindowSizeClass(context)
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> DeviceSize.Compact
                WindowWidthSizeClass.Medium -> DeviceSize.Medium
                WindowWidthSizeClass.Expanded -> DeviceSize.Expanded
                else -> DeviceSize.Compact
            }
        } else {
            // Fallback - context ComponentActivity değilse
            DeviceSize.Compact
        }

}

// Responsive Dp extension
fun Dp.responsive(compact: Dp, medium: Dp, expanded: Dp, deviceSize: DeviceSize): Dp {
    return when(deviceSize) {
        DeviceSize.Compact -> compact
        DeviceSize.Medium -> medium
        DeviceSize.Expanded -> expanded
    }
}

// Responsive Int extension
fun Int.responsive(compact: Int, medium: Int, expanded: Int, deviceSize: DeviceSize): Int {
    return when(deviceSize) {
        DeviceSize.Compact -> compact
        DeviceSize.Medium -> medium
        DeviceSize.Expanded -> expanded
    }
}

// Responsive Float extension
fun Float.responsive(compact: Float, medium: Float, expanded: Float, deviceSize: DeviceSize): Float {
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
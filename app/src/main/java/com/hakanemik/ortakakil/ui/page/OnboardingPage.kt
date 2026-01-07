package com.hakanemik.ortakakil.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.ui.utils.OnboardingScreen1
import com.hakanemik.ortakakil.ui.utils.OnboardingScreen2
import com.hakanemik.ortakakil.ui.utils.OnboardingScreen3
import com.hakanemik.ortakakil.viewmodel.LoginPageViewModel
import com.hakanemik.ortakakil.viewmodel.OnboardingPageViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnboardingPage(
    navController: NavController,
    viewModel: OnboardingPageViewModel = hiltViewModel(),
    onShowSnackbar: (String, SnackbarType) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = 3

    val gradient = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.background_dark),
            colorResource(id = R.color.background_dark).copy(alpha = 0.9f),
            colorResource(id = R.color.surface_dark).copy(alpha = 0.5f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        // Her sayfa için animasyonlu geçiş
        AnimatedVisibility(
            visible = currentPage == 0,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(animationSpec = tween(500))
        ) {
            OnboardingScreen1(onNext = { currentPage++ })
        }
        AnimatedVisibility(
            visible = currentPage == 1,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(animationSpec = tween(500))
        ) {
            OnboardingScreen2(onNext = { currentPage++ }, onBack = { currentPage-- })
        }
        AnimatedVisibility(
            visible = currentPage == 2,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(animationSpec = tween(500))
        ) {
            OnboardingScreen3(
                onLoginClick = { navController.navigate("login_page") },
                onGoogleSignIn =  viewModel::loginWithGoogle ,
                onRegisterClick = { navController.navigate("register_page") },
                onBack = { currentPage-- }
            )
        }

        // Sayfa göstergeleri (dots)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalPages) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (index == currentPage) 10.dp else 8.dp)
                        .background(
                            color = if (index == currentPage) colorResource(id = R.color.primary_purple) else Color.Gray.copy(alpha = 0.5f),
                            shape = MaterialTheme.shapes.extraSmall
                        )
                )
            }
        }
    }
    HandleUIState(uiState.loginState, navController, onShowSnackbar)
}
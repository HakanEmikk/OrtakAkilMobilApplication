package com.hakanemik.ortakakil

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hakanemik.ortakakil.entity.NavItem
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.ui.components.ModernBottomBar
import com.hakanemik.ortakakil.ui.components.ModernTopBar
import com.hakanemik.ortakakil.ui.navigation.Screen
import com.hakanemik.ortakakil.ui.page.AccountInfoPage
import com.hakanemik.ortakakil.ui.page.AnswerPage
import com.hakanemik.ortakakil.ui.page.HomePage
import com.hakanemik.ortakakil.ui.page.LoginPage
import com.hakanemik.ortakakil.ui.page.NotificationSettingsPage
import com.hakanemik.ortakakil.ui.page.ProfilePage
import com.hakanemik.ortakakil.ui.page.RegisterPage
import com.hakanemik.ortakakil.ui.page.SplashPage
import com.hakanemik.ortakakil.ui.theme.OrtakAkilTheme
import com.hakanemik.ortakakil.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lockOrientationForPhones()

        setContent {
            OrtakAkilTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val mainActivityViewModel: MainActivityViewModel = hiltViewModel()

                PageSelect(
                    navController,
                    snackbarHostState,
                    mainActivityViewModel
                )
            }
        }
    }

    private fun lockOrientationForPhones() {
        val configuration = resources.configuration
        val screenWidthDp = configuration.screenWidthDp

        // 600dp altı telefonlar için portrait kilitle
        requestedOrientation = if (screenWidthDp < 600) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // Tablet ve üstü - serbest bırak
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PageSelect(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    mainActivityViewModel: MainActivityViewModel
) {

    val startDestination by mainActivityViewModel.startDestination.collectAsStateWithLifecycle()
    val topBarState by mainActivityViewModel.topBarState.collectAsStateWithLifecycle()
    val bottomBarState by mainActivityViewModel.bottomBarState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val deviceSize = currentDeviceSizeHelper()

    // Listen to UI Effects
    LaunchedEffect(Unit) {
        mainActivityViewModel.uiEvent.collectLatest { effect ->
            when (effect) {
                is MainActivityViewModel.UiEffect.NavigateTo -> {
                    navController.navigate(effect.route) {
                        if (effect.popUpToRoute != null) {
                            popUpTo(if(effect.popUpToRoute == "0") 0 else effect.popUpToRoute) {
                                inclusive = effect.inclusive
                            }
                        }
                    }
                }
                MainActivityViewModel.UiEffect.GoBack -> {
                    navController.popBackStack()
                }
                is MainActivityViewModel.UiEffect.ShowSnackbar -> {
                    launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }
        }
    }

    val bottomItems = listOf(
        NavItem(Screen.Home.route, "Ana Sayfa", R.drawable.outline_home),
        NavItem(Screen.History.route, "Geçmiş",  R.drawable.world), // Assuming History page route
        NavItem(Screen.NotificationSettings.route, "Ayarlar", R.drawable.notification) // Temporarily pointing to settings for the icon
    )

    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (topBarState.isVisible) {
                ModernTopBar(
                    topBarState = topBarState,
                    deviceSize = deviceSize
                )
            }
        },
        bottomBar = {
            if (bottomBarState.isVisible) {
                ModernBottomBar(
                    navController = navController,
                    bottomItems = bottomItems
                )
            }
        }
    ) { innerPadding ->
        if (startDestination == null) {
            LaunchedEffect(Unit) {
                mainActivityViewModel.hideTopBar()
                mainActivityViewModel.hideBottomBar()
            }
            SplashPage()
            return@Scaffold
        }

        NavHost(
            navController = navController,
            startDestination = startDestination!!,
            modifier = Modifier.padding(
                top = if (topBarState.isVisible) innerPadding.calculateTopPadding() else 0.dp,
                bottom = if (bottomBarState.isVisible) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {
            composable(route = Screen.Login.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.hideTopBar()
                    mainActivityViewModel.hideBottomBar()
                }
                LoginPage(
                    navController = navController,
                    onShowSnackbar = { msg -> mainActivityViewModel.showSnackbar(msg) }
                )
            }
            composable(route = Screen.Register.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.hideTopBar()
                    mainActivityViewModel.hideBottomBar()
                }
                RegisterPage(
                    navController = navController,
                    onShowSnackbar = { msg -> mainActivityViewModel.showSnackbar(msg) }
                )
            }
            composable(route = Screen.Profile.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Profil",
                        leftIcon = R.drawable.arrow_back,
                        onLeftClick = { mainActivityViewModel.answerPageBackNavigate() },
                        rightIcon = R.drawable.logout,
                        onRightClick = { mainActivityViewModel.logout() }
                    )
                    mainActivityViewModel.hideBottomBar()
                }
                ProfilePage(navController, snackbarHostState)
            }
            composable(route = Screen.NotificationSettings.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Bildirim Ayarları",
                        leftIcon = R.drawable.arrow_back,
                        onLeftClick = { mainActivityViewModel.answerPageBackNavigate() }
                    )
                    mainActivityViewModel.hideBottomBar()
                }
                NotificationSettingsPage()
            }
            composable(route = Screen.AccountInfo.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Hesap Bilgileri",
                        leftIcon = R.drawable.arrow_back,
                        onLeftClick = { mainActivityViewModel.answerPageBackNavigate() }
                    )
                    mainActivityViewModel.hideBottomBar()
                }
                AccountInfoPage(navController)
            }
            composable(route = Screen.Home.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Ana Sayfa",
                        leftIcon = R.drawable.person,
                        rightIcon = R.drawable.settings,
                        onLeftClick = { navController.navigate(Screen.Profile.route) },
                        onRightClick = { /* Settings */ },
                        isVisible = true
                    )
                    mainActivityViewModel.showBottomBar()
                }
                HomePage(navController, snackbarHostState)
            }
            composable(route = Screen.History.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Geçmiş Sorular",
                        isVisible = true
                    )
                    mainActivityViewModel.showBottomBar()
                }
                // NOTE: Originally it was LoginPage here? Please check. Assuming placeholder.
                LoginPage(navController,onShowSnackbar = { msg -> mainActivityViewModel.showSnackbar(msg)})
            }
            composable(
                route = Screen.Answer.route,
                arguments = listOf(navArgument("question") { type = NavType.StringType })
            ) { backStackEntry ->
                val question = backStackEntry.arguments?.getString("question")
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Yapay Zekanın Önerisi",
                        leftIcon = R.drawable.arrow_back,
                        onLeftClick = { mainActivityViewModel.answerPageBackNavigate() },
                    )
                    mainActivityViewModel.hideBottomBar()
                }
                AnswerPage(navController, snackbarHostState, question)
            }
        }
    }
}




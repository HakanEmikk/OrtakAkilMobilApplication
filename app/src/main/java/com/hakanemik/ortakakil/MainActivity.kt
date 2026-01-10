package com.hakanemik.ortakakil

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.hakanemik.ortakakil.entity.AnswerItem
import com.hakanemik.ortakakil.entity.DiscoveryResponse
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.entity.HistoryResponse
import com.hakanemik.ortakakil.entity.NavItem
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.ui.components.ModernBottomBar
import com.hakanemik.ortakakil.ui.components.ModernTopBar
import com.hakanemik.ortakakil.ui.navigation.Screen
import com.hakanemik.ortakakil.ui.page.AccountInfoPage
import com.hakanemik.ortakakil.ui.page.AnswerPage
import com.hakanemik.ortakakil.ui.page.DiscoveryDetailPage
import com.hakanemik.ortakakil.ui.page.DiscoveryPage
import com.hakanemik.ortakakil.ui.page.HistoryDetailPage
import com.hakanemik.ortakakil.ui.page.HistoryPage
import com.hakanemik.ortakakil.ui.page.HomePage
import com.hakanemik.ortakakil.ui.page.LoginPage
import com.hakanemik.ortakakil.ui.page.NotificationSettingsPage
import com.hakanemik.ortakakil.ui.page.OnboardingPage
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

        // Install splash screen
        installSplashScreen()
        enableEdgeToEdge()
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

    var currentSnackbarType by remember { mutableStateOf(SnackbarType.INFO) }

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
                    currentSnackbarType = effect.type
                    launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }
        }
    }

    val bottomItems = listOf(
        NavItem(Screen.Home.route, "Ana Sayfa", R.drawable.outline_home),
        NavItem(Screen.Discovery.route, "Keşfet",  R.drawable.world), // Assuming History page route
        NavItem(Screen.History.route, "Geçmişim", R.drawable.history) // Temporarily pointing to settings for the icon
    )

    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(id = R.color.background_dark),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val backgroundColor = when (currentSnackbarType) {
                    SnackbarType.SUCCESS -> colorResource(id = R.color.success)
                    SnackbarType.ERROR -> colorResource(id = R.color.error)
                    SnackbarType.WARNING -> colorResource(id = R.color.warning)
                    SnackbarType.INFO -> colorResource(id = R.color.primary_purple)
                }
                // Custom Snackbar
                Snackbar(
                    snackbarData = data,
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
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
            startDestination = startDestination ?: Screen.Onboarding.route,
            modifier = Modifier.padding(
                top = if (topBarState.isVisible) innerPadding.calculateTopPadding() else 0.dp,
                bottom = if (bottomBarState.isVisible) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {
            composable(route= Screen.History.route){
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        isVisible = true,
                        title ="Geçmişim"
                    )
                    mainActivityViewModel.showBottomBar()
                }
                HistoryPage(navController, onShowSnackbar = { msg, type -> mainActivityViewModel.showSnackbar(msg, type)})
            }
            composable(route= Screen.Onboarding.route){
                LaunchedEffect(Unit) {
                    mainActivityViewModel.hideTopBar()
                    mainActivityViewModel.hideBottomBar()
                }
                OnboardingPage(navController, onShowSnackbar = { msg, type -> mainActivityViewModel.showSnackbar(msg, type)})
            }
            composable(route = Screen.Login.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.hideTopBar()
                    mainActivityViewModel.hideBottomBar()
                }
                LoginPage(
                    navController = navController,
                    onShowSnackbar =  { msg, type -> mainActivityViewModel.showSnackbar(msg, type) }
                )
            }
            composable(route = Screen.Register.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.hideTopBar()
                    mainActivityViewModel.hideBottomBar()
                }
                RegisterPage(
                    navController = navController,
                    onShowSnackbar = { msg, type -> mainActivityViewModel.showSnackbar(msg, type) }
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
                ProfilePage(navController, mainActivityViewModel::showSnackbar)
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
                AccountInfoPage(navController, mainActivityViewModel::showSnackbar)
            }
            composable(route = Screen.Home.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        leftIcon = R.drawable.person,
                        onLeftClick = { navController.navigate(Screen.Profile.route) },
                        isVisible = true,
                        isHomePage = true,
                        title =""
                    )
                    mainActivityViewModel.showBottomBar()
                }
                HomePage(navController, { msg, type -> mainActivityViewModel.showSnackbar(msg, type)})
            }
            composable(route = Screen.Discovery.route) {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Keşfet",
                        isVisible = true
                    )
                    mainActivityViewModel.showBottomBar()
                }
                DiscoveryPage(navController,onShowSnackbar = { msg, type -> mainActivityViewModel.showSnackbar(msg, type)})
            }
            composable(
                route = Screen.Answer.route,
                arguments = listOf(navArgument("answerItem") { type = NavType.StringType })
            ) { backStackEntry ->
                val json = backStackEntry.arguments?.getString("answerItem")
                if (json != null){
                    val answerItem = Gson().fromJson(json, AnswerItem::class.java)
                    LaunchedEffect(Unit) {
                        mainActivityViewModel.setTopBar(
                            title = "Ortak Akıl'ın Önerisi",
                            leftIcon = R.drawable.arrow_back,
                            onLeftClick = { mainActivityViewModel.answerPageBackNavigate() },
                        )
                        mainActivityViewModel.hideBottomBar()
                    }
                    AnswerPage(navController, onShowSnackbar = { msg, type -> mainActivityViewModel.showSnackbar(msg, type)},answerItem)
                }
            }
            composable(
                route = Screen.DiscoveryDetail.route,
                arguments = listOf(navArgument("discoveryJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val json = backStackEntry.arguments?.getString("discoveryJson")
                if (json != null) {
                    val discoveryItem = Gson().fromJson(json, DiscoveryResponse::class.java)
                    
                    LaunchedEffect(Unit) {
                        mainActivityViewModel.setTopBar(
                            title = "Detay",
                            leftIcon = R.drawable.arrow_back,
                            onLeftClick = { navController.popBackStack() }
                        )
                        mainActivityViewModel.hideBottomBar()
                    }
                    DiscoveryDetailPage(navController, discoveryItem)
                }
            }
            composable(
                route = Screen.HistoryDetail.route,
                arguments = listOf(navArgument("historyJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val json = backStackEntry.arguments?.getString("historyJson")
                if (json != null) {
                    val historyItem = Gson().fromJson(json, HistoryResponse::class.java)

                    LaunchedEffect(Unit) {
                        mainActivityViewModel.setTopBar(
                            title = "Detay",
                            leftIcon = R.drawable.arrow_back,
                            onLeftClick = { navController.popBackStack() }
                        )
                        mainActivityViewModel.hideBottomBar()
                    }
                    HistoryDetailPage(navController, historyItem, onShowSnackbar = { msg, type -> mainActivityViewModel.showSnackbar(msg, type) })
                }
            }
        }
    }
}




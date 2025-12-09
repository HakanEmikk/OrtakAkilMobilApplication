package com.hakanemik.ortakakil


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hakanemik.ortakakil.entity.NavItem
import com.hakanemik.ortakakil.entity.TopBarState
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.ui.page.AnswerPage
import com.hakanemik.ortakakil.ui.page.HomePage
import com.hakanemik.ortakakil.ui.page.LoginPage
import com.hakanemik.ortakakil.ui.page.ProfilePage
import com.hakanemik.ortakakil.ui.page.RegisterPage
import com.hakanemik.ortakakil.ui.page.SplashPage
import com.hakanemik.ortakakil.ui.theme.OrtakAkilTheme
import com.hakanemik.ortakakil.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lockOrientationForPhones()

        setContent {
            OrtakAkilTheme {
                val navController= rememberNavController()
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
 fun PageSelect(navController: NavHostController, snackbarHostState: SnackbarHostState,mainActivityViewModel: MainActivityViewModel) {

    val startDestination by mainActivityViewModel.startDestination.collectAsStateWithLifecycle()
    val topBarState by mainActivityViewModel.topBarState.collectAsStateWithLifecycle()
    val bottomBarState by mainActivityViewModel.bottomBarState.collectAsStateWithLifecycle()

    val deviceSize = currentDeviceSizeHelper()

    val bottomItems = listOf(
        NavItem("home_page", "Ana Sayfa", R.drawable.outline_home),
        NavItem("history_page", "Geçmiş",  R.drawable.world),
        NavItem("register_page", "Ayarlar", R.drawable.notification)
    )
    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)},
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
    ) {
        innerPadding ->
        if (startDestination == null){
            LaunchedEffect(Unit) {
                mainActivityViewModel.hideTopBar()
                mainActivityViewModel.hideBottomBar()
            }
            SplashPage()
            return@Scaffold
        }

        NavHost(navController=navController,
            startDestination = startDestination!!,
            modifier = Modifier.
            padding(top = if (topBarState.isVisible) innerPadding.calculateTopPadding() else 0.dp,
                bottom = if (bottomBarState.isVisible) innerPadding.calculateBottomPadding() else 0.dp)){
            composable(route = "login_page"){
                LaunchedEffect(Unit) {
                    mainActivityViewModel.hideTopBar()
                    mainActivityViewModel.hideBottomBar()
                }
                LoginPage(navController,snackbarHostState)
            }
            composable(route = "register_page"){
                LaunchedEffect(Unit) {
                    mainActivityViewModel.hideTopBar()
                    mainActivityViewModel.hideBottomBar()
                }
                RegisterPage(navController,snackbarHostState)
            }
            composable(route = "profile_page"){
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Profil",
                        leftIcon = R.drawable.arrow_back,
                        onLeftClick = {}
                    )
                    mainActivityViewModel.hideBottomBar()
                }
                ProfilePage(navController,snackbarHostState)
            }
            composable(route="home_page"){
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Ana Sayfa",
                        leftIcon = R.drawable.person,
                        rightIcon = R.drawable.settings,
                        onLeftClick = { navController.navigate("profile_page") },
                        onRightClick = { /* Ayarlar */ },
                        isVisible = true
                    )
                    mainActivityViewModel.showBottomBar()
                }
                HomePage(navController,snackbarHostState)
            }
            composable(route = "history_page") {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Geçmiş Sorular",
                        isVisible = true
                    )
                    mainActivityViewModel.showBottomBar()
                }
                LoginPage(navController, snackbarHostState)
            }
            composable(route = "answer_page/{question}",
                arguments = listOf(navArgument("question"){type = NavType.StringType})
            ) {backStackEntry ->
                val question = backStackEntry.arguments?.getString("question")
                LaunchedEffect(Unit) {
                    mainActivityViewModel.setTopBar(
                        title = "Yapay Zekanın Önerisi",
                        leftIcon = R.drawable.arrow_back,
                        onLeftClick = { mainActivityViewModel.answerPageBackNavigate(navController) },
                    )
                    mainActivityViewModel.hideBottomBar()
                }
                AnswerPage(navController,snackbarHostState,question)

            }
            /*composable("details_page/{food}", arguments = listOf(
                navArgument("food"){
                    type= NavType.StringType
                }
            )){
                val json=it.arguments?.getString("food")
                val food= Gson().fromJson(json, Foods::class.java)
                DetailsPage(food)
            }*/
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(topBarState: TopBarState,deviceSize: DeviceSize){
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

@Composable
fun ModernBottomBar(navController: NavHostController,bottomItems: List<NavItem>){

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(id = R.color.surface_dark),
        shadowElevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            // Gradient indicator background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
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
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
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
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.gradient_start),
                                    colorResource(id = R.color.gradient_end)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.label,
                    tint = if (isSelected)
                        colorResource(id = R.color.white)
                    else
                        colorResource(id = R.color.text_secondary),
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer {
                            scaleX = animatedScale
                            scaleY = animatedScale
                            alpha = animatedAlpha
                        }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected)
                    colorResource(id = R.color.primary_purple)
                else
                    colorResource(id = R.color.text_secondary),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp,
                modifier = Modifier.graphicsLayer {
                    alpha = animatedAlpha
                }
            )
        }
    }
}




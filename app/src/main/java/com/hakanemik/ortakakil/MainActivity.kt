package com.hakanemik.ortakakil


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hakanemik.ortakakil.ui.page.AnswerPage
import com.hakanemik.ortakakil.ui.page.HomePage
import com.hakanemik.ortakakil.ui.page.LoginPage
import com.hakanemik.ortakakil.ui.page.RegisterPage
import com.hakanemik.ortakakil.ui.page.SplashPage
import com.hakanemik.ortakakil.ui.theme.OrtakAkilTheme
import com.hakanemik.ortakakil.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) {  innerPadding ->
                    pageSelect(navController,snackbarHostState)
                }

            }
        }
    }
    private fun lockOrientationForPhones() {
        val configuration = resources.configuration
        val screenWidthDp = configuration.screenWidthDp

        // 600dp altı telefonlar için portrait kilitle
        if (screenWidthDp < 600) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // Tablet ve üstü - serbest bırak
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
 fun pageSelect(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val startDestination by authViewModel.startDestination.collectAsStateWithLifecycle()

    if (startDestination == null){
        SplashPage()
        return
    }

    NavHost(navController=navController,startDestination = startDestination!!){
        composable(route = "login_page"){
            LoginPage(navController,snackbarHostState)
        }
        composable(route = "register_page"){
            RegisterPage(navController,snackbarHostState)
        }
        composable(route="home_page"){
            HomePage(navController,snackbarHostState)
        }
        composable(route = "answer_page/{question}",
            arguments = listOf(navArgument("question"){type = NavType.StringType})
            ) {backStackEntry ->
            val question = backStackEntry.arguments?.getString("question")
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



package com.hakanemik.ortakakil


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.hakanemik.ortakakil.ui.theme.OrtakAkilTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                    snackbarHost = { androidx.compose.material3.SnackbarHost(hostState = snackbarHostState) }
                ) {  innerPadding ->
                    PageSelect(navController,snackbarHostState)
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

@Composable
fun PageSelect(navController: NavHostController, snackbarHostState: SnackbarHostState) {

    NavHost(navController=navController,startDestination = "login_page"){
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



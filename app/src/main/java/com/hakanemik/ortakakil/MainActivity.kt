package com.hakanemik.ortakakil

import LoginPage
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.hakanemik.ortakakil.ui.page.HomePage

import com.hakanemik.ortakakil.ui.page.RegisterPage
import com.hakanemik.ortakakil.ui.theme.OrtakAkilTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}

@Composable
fun PageSelect(navController: NavHostController, snackbarHostState: SnackbarHostState) {

    NavHost(navController=navController,startDestination = "login_page"){
        composable("login_page"){
            LoginPage(navController,snackbarHostState)
        }
        composable("register_page"){
            RegisterPage(navController,snackbarHostState)
        }
        composable("home_page"){
            HomePage(navController,snackbarHostState)
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



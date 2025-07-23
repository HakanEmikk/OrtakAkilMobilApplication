package com.hakanemik.ortakakil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.hakanemik.ortakakil.ui.page.LoginPage
import com.hakanemik.ortakakil.ui.page.RegisterPage
import com.hakanemik.ortakakil.ui.theme.OrtakAkilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrtakAkilTheme {
                PageSelect()
            }
        }
    }
}

@Composable
fun PageSelect() {
    val navController= rememberNavController()
    NavHost(navController=navController,startDestination = "login_page"){
        composable("login_page"){
            LoginPage(navController)
        }
        composable("register_page"){
            RegisterPage(navController)
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


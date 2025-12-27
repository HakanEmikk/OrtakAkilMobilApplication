package com.hakanemik.ortakakil.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login_page")
    data object Register : Screen("register_page")
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object History : Screen("history_page")
    data object NotificationSettings : Screen("notification_settings_page")
    data object AccountInfo : Screen("account_info_page")
    data object Splash : Screen("splash_page")

    // Dynamic routes (handling arguments)
    data object Answer : Screen("answer_page/{question}") {
        fun createRoute(question: String) = "answer_page/$question"
    }
}
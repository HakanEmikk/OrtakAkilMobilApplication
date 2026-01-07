package com.hakanemik.ortakakil.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login_page")
    data object Register : Screen("register_page")
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object History : Screen("history_page")
    data object Discovery : Screen("discovery_page")
    data object NotificationSettings : Screen("notification_settings_page")
    data object AccountInfo : Screen("account_info_page")
    data object Splash : Screen("splash_page")
    data object Onboarding : Screen("onboarding_page")

    // Dynamic routes (handling arguments)
    data object Answer : Screen("answer_page/{answerItem}") {
        fun createRoute(answerItem: String) = "answer_page/$answerItem"
    }
    data object DiscoveryDetail : Screen("discovery_detail_page/{discoveryJson}") {
        fun createRoute(discoveryJson: String) = "discovery_detail_page/$discoveryJson"
    }
    data object HistoryDetail : Screen("history_detail_page/{historyJson}") {
        fun createRoute(historyJson: String) = "history_detail_page/$historyJson"
    }
}
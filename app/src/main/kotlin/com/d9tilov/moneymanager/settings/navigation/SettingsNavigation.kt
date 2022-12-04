package com.d9tilov.moneymanager.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.moneymanager.settings.ui.SettingsRoute

const val settingsNavigationRoute = "settings"

fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(settingsNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(clickBack: () -> Unit) {
    composable(route = settingsNavigationRoute) { SettingsRoute(clickBack = clickBack) }
}

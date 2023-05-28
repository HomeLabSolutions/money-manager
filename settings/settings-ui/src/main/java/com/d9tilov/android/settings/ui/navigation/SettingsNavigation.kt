package com.d9tilov.android.settings.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common_android.ui.base.BaseNavigator
import com.d9tilov.android.settings.ui.SettingsRoute

const val settingsNavigationRoute = "settings"

interface SettingsNavigator : BaseNavigator {
    fun save()
}
fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(settingsNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(clickBack: () -> Unit) {
    composable(route = settingsNavigationRoute) { SettingsRoute(clickBack = clickBack) }
}

interface SettingsBillingNavigator : BaseNavigator

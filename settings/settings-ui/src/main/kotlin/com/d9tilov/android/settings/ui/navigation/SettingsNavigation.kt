package com.d9tilov.android.settings.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.settings.ui.SettingsRoute

interface SettingsBillingNavigator : BaseNavigator

const val settingsNavigationRoute = "settings_route"

fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(settingsNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(clickBack: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
    composable(route = settingsNavigationRoute) { SettingsRoute(clickBack = clickBack, onShowSnackBar = onShowSnackBar) }
}

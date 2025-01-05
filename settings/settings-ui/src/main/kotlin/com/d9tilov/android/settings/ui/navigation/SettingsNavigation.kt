package com.d9tilov.android.settings.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.settings.ui.SettingsRoute

interface SettingsBillingNavigator : BaseNavigator

const val SETTINGS_NAVIGATION_ROUTE = "settings_route"

fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    route: String,
    clickBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    composable(route = route) { SettingsRoute(clickBack = clickBack, onShowSnackBar = onShowSnackBar) }
}

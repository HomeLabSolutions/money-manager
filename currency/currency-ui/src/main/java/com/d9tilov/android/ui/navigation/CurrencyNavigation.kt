package com.d9tilov.android.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.ui.CurrencyListRoute

const val currencyNavigationRoute = "currency_list"

fun NavController.navigateToCurrencyListScreen(navOptions: NavOptions? = null) {
    this.navigate(com.d9tilov.android.ui.navigation.currencyNavigationRoute, navOptions)
}

fun NavGraphBuilder.currencyScreen(clickBack: () -> Unit) {
    composable(route = com.d9tilov.android.ui.navigation.currencyNavigationRoute) { CurrencyListRoute(clickBack = clickBack) }
}

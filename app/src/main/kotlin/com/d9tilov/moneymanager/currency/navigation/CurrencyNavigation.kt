package com.d9tilov.moneymanager.currency.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.moneymanager.currency.ui.CurrencyListRoute

const val currencyNavigationRoute = "currency_list"

fun NavController.navigateToCurrencyList(navOptions: NavOptions? = null) {
    this.navigate(currencyNavigationRoute, navOptions)
}

fun NavGraphBuilder.currencyScreen() {
    composable(route = currencyNavigationRoute) { CurrencyListRoute() }
}

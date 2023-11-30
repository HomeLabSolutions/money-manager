package com.d9tilov.android.currency.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.d9tilov.android.currency.ui.CurrencyListRoute

const val currencyNavigationRoute = "currency_list"
internal const val currencyCodeArg = "currency_code"

internal sealed class CurrencyArgs {
    class CurrencyScreenArgs(val currencyCode: String?) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(savedStateHandle.get(currencyCodeArg) as? String)
    }
}

fun NavGraphBuilder.currencyScreen(clickBack: () -> Unit, onChooseCurrency: (String) -> Unit) {
    composable(
        route = "$currencyNavigationRoute?{$currencyCodeArg}",
        arguments = listOf(navArgument(currencyCodeArg) { nullable = true })
    ) { CurrencyListRoute(clickBack = clickBack, onChooseCurrency = onChooseCurrency) }
}
fun NavController.navigateToCurrencyListScreen(currencyCode: String? = null, navOptions: NavOptions? = null) {
    this.navigate("$currencyNavigationRoute?$currencyCode", navOptions)
}

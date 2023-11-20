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
internal const val changeCurrencyGloballyArg = "change_currency_globally"

internal sealed class CurrencyArgs {
    class CurrencyScreenArgs(val changeGlobally: Boolean) {
        constructor(savedStateHandle: SavedStateHandle) :
                this((checkNotNull(savedStateHandle[changeCurrencyGloballyArg]) as Boolean))
    }
}

fun NavGraphBuilder.currencyScreen(clickBack: () -> Unit, onChooseCurrency: (String) -> Unit) {
    composable(
        route = "$currencyNavigationRoute/{$changeCurrencyGloballyArg}",
        arguments = listOf(navArgument(changeCurrencyGloballyArg) { type = NavType.BoolType })
    ) { CurrencyListRoute(clickBack = clickBack, onChooseCurrency = onChooseCurrency) }
}
fun NavController.navigateToCurrencyListScreen(changeCurrencyGlobally: Boolean = false, navOptions: NavOptions? = null) {
    this.navigate("$currencyNavigationRoute/$changeCurrencyGlobally", navOptions)
}

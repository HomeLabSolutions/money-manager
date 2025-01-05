package com.d9tilov.android.currency.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.d9tilov.android.currency.domain.model.CurrencyArgs.CURRENCY_CODE_ARGS
import com.d9tilov.android.currency.ui.CurrencyListRoute

const val CURRENCY_LIST_NAVIGATION_ROUTE = "currency_list_route"

internal sealed class CurrencyArgs {
    class CurrencyScreenArgs(
        val currencyCode: String?,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle.get(CURRENCY_CODE_ARGS) as? String)
    }
}

fun NavGraphBuilder.currencyScreen(
    route: String,
    clickBack: () -> Unit,
    onChooseCurrency: (String) -> Unit,
) {
    composable(
        route = route,
        arguments =
            listOf(
                navArgument(CURRENCY_CODE_ARGS) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
    ) { CurrencyListRoute(clickBack = clickBack, onChooseCurrency = onChooseCurrency) }
}

fun NavController.navigateToCurrencyListScreen(
    currencyCode: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate("$CURRENCY_LIST_NAVIGATION_ROUTE?$currencyCode", navOptions)
}

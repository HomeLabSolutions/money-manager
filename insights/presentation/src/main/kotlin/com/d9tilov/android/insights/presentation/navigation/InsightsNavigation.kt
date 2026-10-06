package com.d9tilov.android.insights.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.d9tilov.android.insights.presentation.InsightsRoute

const val INSIGHTS_NAVIGATION_ROUTE = "insights_route"
const val INSIGHTS_DEEP_LINK_URI = "moneymanager://insights"

fun NavController.navigateToInsights(navOptions: NavOptions? = null) {
    navigate(INSIGHTS_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.insightsScreen(
    onBackClick: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    composable(
        route = INSIGHTS_NAVIGATION_ROUTE,
        deepLinks = listOf(navDeepLink { uriPattern = INSIGHTS_DEEP_LINK_URI }),
    ) {
        InsightsRoute(onBackClick = onBackClick, onShowSnackBar = onShowSnackBar)
    }
}

package com.d9tilov.android.insights.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.d9tilov.android.designsystem.component.roundedBackComposable
import com.d9tilov.android.insights.presentation.InsightsRoute

const val INSIGHTS_NAVIGATION_ROUTE = "insights_route"

fun NavController.navigateToInsights(navOptions: NavOptions? = null) {
    navigate(INSIGHTS_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.insightsScreen(
    onBackClick: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    roundedBackComposable(route = INSIGHTS_NAVIGATION_ROUTE) {
        InsightsRoute(onBackClick = onBackClick, onShowSnackBar = onShowSnackBar)
    }
}

package com.d9tilov.moneymanager.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.d9tilov.android.incomeexpense.navigation.incomeExpenseNavigationRoute
import com.d9tilov.android.incomeexpense.navigation.navigateToIncomeExpense
import com.d9tilov.android.profile.ui.navigation.navigateToProfile
import com.d9tilov.android.profile.ui.navigation.profileNavigationRoute
import com.d9tilov.android.statistics.ui.navigation.navigateToStatistics
import com.d9tilov.android.statistics.ui.navigation.statisticsNavigationRoute
import com.d9tilov.moneymanager.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberMmAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): MmAppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
    ) {
        MmAppState(
            navController,
            coroutineScope,
            windowSizeClass,
        )
    }
}

@Stable
class MmAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            incomeExpenseNavigationRoute -> TopLevelDestination.INCOME_EXPENSE
            statisticsNavigationRoute -> TopLevelDestination.STATISTICS
            profileNavigationRoute -> TopLevelDestination.PROFILE
            else -> null
        }

    val shouldShowBottomBar: Boolean =
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.INCOME_EXPENSE -> navController.navigateToIncomeExpense(
                    topLevelNavOptions
                )

                TopLevelDestination.STATISTICS -> navController.navigateToStatistics(
                    topLevelNavOptions
                )

                TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
            }
        }
    }
}

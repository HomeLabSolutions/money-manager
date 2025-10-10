package com.d9tilov.android.statistics.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.statistics.ui.StatisticsDetailsRoute
import com.d9tilov.android.statistics.ui.StatisticsRoute
import com.d9tilov.android.statistics.ui.model.TransactionDetailsChartModel
import kotlinx.datetime.LocalDateTime

const val STATISTICS_NAVIGATION_ROUTE = "statistics_route"
const val STATISTICS_DETAILS_NAVIGATION_ROUTE = "statistics_details_route"
const val TRANSACTION_DETAILS_CATEGORY_ID_ARGS = "transaction_details_category_id"
const val TRANSACTION_DETAILS_DATE_FROM_ARGS = "transaction_details_date_from"
const val TRANSACTION_DETAILS_DATE_TO_ARGS = "transaction_details_date_to"
const val TRANSACTION_DETAILS_IN_STATISTICS_ARGS = "transaction_details_in_statistics"

fun NavController.navigateToStatistics(navOptions: NavOptions? = null) {
    this.navigate(STATISTICS_NAVIGATION_ROUTE, navOptions)
}

internal data class TransactionDetailsArgs(
    val categoryId: Long,
    val dateFrom: Long,
    val dateTo: Long,
    val inStatistics: Boolean,
) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            checkNotNull(savedStateHandle[TRANSACTION_DETAILS_CATEGORY_ID_ARGS]).toString().toLong(),
            checkNotNull(savedStateHandle[TRANSACTION_DETAILS_DATE_FROM_ARGS]).toString().toLong(),
            checkNotNull(savedStateHandle[TRANSACTION_DETAILS_DATE_TO_ARGS]).toString().toLong(),
            checkNotNull(savedStateHandle[TRANSACTION_DETAILS_IN_STATISTICS_ARGS]).toString().toBoolean(),
        )
}

fun NavController.navigateToStatisticsDetailsTransactionScreen(
    model: TransactionDetailsChartModel,
    from: LocalDateTime,
    to: LocalDateTime,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        "$STATISTICS_DETAILS_NAVIGATION_ROUTE/" +
            "${model.categoryId}/" +
            "${from.toMillis()}/" +
            "${to.toMillis()}/" +
            "${model.inStatistics}",
        navOptions,
    )
}

fun NavGraphBuilder.statisticsScreen(
    route: String,
    onTransactionClick: (TransactionDetailsChartModel, LocalDateTime, LocalDateTime) -> Unit,
) {
    composable(route = route) { StatisticsRoute(onTransactionClicked = onTransactionClick) }
}

fun NavGraphBuilder.statisticsDetailsScreen(
    route: String,
    onBackClicked: () -> Unit,
) {
    composable(route = route) { StatisticsDetailsRoute(onBackClicked = onBackClicked) }
}

package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class StatisticsChartMode(val name: String) : Parcelable {

    companion object {
        private const val STATISTICS_PIE_CHART_NAME = "statistics_pie_chart"
        private const val STATISTICS_LINE_CHART_NAME = "statistics_line_chart"
    }

    @Parcelize
    object PIE_CHART : StatisticsChartMode(STATISTICS_PIE_CHART_NAME)

    @Parcelize
    object LINE_CHART : StatisticsChartMode(STATISTICS_LINE_CHART_NAME)
}

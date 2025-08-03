package com.d9tilov.android.statistics.ui.model

import androidx.annotation.StringRes
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.statistics.ui.R
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

sealed class StatisticsPeriodModel(
    @field:StringRes val name: Int,
    val from: LocalDateTime,
    val to: LocalDateTime,
) {
    data class DAY(
        val fromDate: LocalDateTime = currentDate().getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_day,
            fromDate,
            toDate,
        )

    data class WEEK(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.WEEK).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_week,
            fromDate,
            toDate,
        )

    data class MONTH(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_month,
            fromDate,
            toDate,
        )

    data class YEAR(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.YEAR).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_year,
            fromDate,
            toDate,
        )

    data class CUSTOM(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_custom,
            fromDate.getStartOfDay(),
            toDate.getEndOfDay(),
        )
}

infix fun StatisticsPeriodModel.plus(count: Int): StatisticsPeriodModel =
    when (this) {
        is StatisticsPeriodModel.CUSTOM -> this
        is StatisticsPeriodModel.DAY ->
            StatisticsPeriodModel.DAY(
                from.date
                    .plus(count, DateTimeUnit.DAY)
                    .getStartOfDay(),
                to.date
                    .plus(count, DateTimeUnit.DAY)
                    .getEndOfDay(),
            )

        is StatisticsPeriodModel.WEEK ->
            StatisticsPeriodModel.WEEK(
                from.date
                    .plus(count, DateTimeUnit.WEEK)
                    .getStartOfDay(),
                to.date
                    .plus(count, DateTimeUnit.WEEK)
                    .getEndOfDay(),
            )

        is StatisticsPeriodModel.MONTH ->
            StatisticsPeriodModel.MONTH(
                from.date
                    .plus(count, DateTimeUnit.MONTH)
                    .getStartOfDay(),
                to.date
                    .plus(count, DateTimeUnit.MONTH)
                    .getEndOfDay(),
            )

        is StatisticsPeriodModel.YEAR ->
            StatisticsPeriodModel.YEAR(
                from.date
                    .plus(count, DateTimeUnit.YEAR)
                    .getStartOfDay(),
                to.date
                    .plus(count, DateTimeUnit.YEAR)
                    .getEndOfDay(),
            )
    }

infix fun StatisticsPeriodModel.minus(count: Int): StatisticsPeriodModel =
    when (this) {
        is StatisticsPeriodModel.CUSTOM -> this
        is StatisticsPeriodModel.DAY ->
            StatisticsPeriodModel.DAY(
                from.date
                    .minus(count, DateTimeUnit.DAY)
                    .getStartOfDay(),
                to.date
                    .minus(count, DateTimeUnit.DAY)
                    .getEndOfDay(),
            )

        is StatisticsPeriodModel.WEEK ->
            StatisticsPeriodModel.WEEK(
                from.date
                    .minus(count, DateTimeUnit.WEEK)
                    .getStartOfDay(),
                to.date
                    .minus(count, DateTimeUnit.WEEK)
                    .getEndOfDay(),
            )

        is StatisticsPeriodModel.MONTH ->
            StatisticsPeriodModel.MONTH(
                from.date
                    .minus(count, DateTimeUnit.MONTH)
                    .getStartOfDay(),
                to.date
                    .minus(count, DateTimeUnit.MONTH)
                    .getEndOfDay(),
            )

        is StatisticsPeriodModel.YEAR ->
            StatisticsPeriodModel.YEAR(
                from.date
                    .minus(count, DateTimeUnit.YEAR)
                    .getStartOfDay(),
                to.date
                    .minus(count, DateTimeUnit.YEAR)
                    .getEndOfDay(),
            )
    }

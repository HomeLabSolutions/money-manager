package com.d9tilov.android.statistics.ui.model

import androidx.annotation.DrawableRes
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics_ui.R

sealed class StatisticsMenuTransactionType(
    @DrawableRes val iconId: Int,
) : BaseStatisticsMenuType {
    data object Expense : StatisticsMenuTransactionType(R.drawable.ic_statistics_expense)

    data object Income : StatisticsMenuTransactionType(R.drawable.ic_statistics_income)

    override val menuType: StatisticsMenuType = StatisticsMenuType.TRANSACTION_TYPE
}

fun StatisticsMenuTransactionType.toTransactionType() =
    when (this) {
        StatisticsMenuTransactionType.Expense -> TransactionType.EXPENSE
        StatisticsMenuTransactionType.Income -> TransactionType.INCOME
    }

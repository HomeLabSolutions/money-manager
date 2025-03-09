package com.d9tilov.android.statistics.data.model

sealed class StatisticsMenuTransactionType : BaseStatisticsMenuType {
    object Expense : StatisticsMenuTransactionType()

    object Income : StatisticsMenuTransactionType()

    override val menuType: StatisticsMenuType = StatisticsMenuType.TRANSACTION_TYPE
}

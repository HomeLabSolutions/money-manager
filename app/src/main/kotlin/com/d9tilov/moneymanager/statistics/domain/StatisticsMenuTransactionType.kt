package com.d9tilov.moneymanager.statistics.domain

sealed class StatisticsMenuTransactionType : BaseStatisticsMenuType {

    object Expense : StatisticsMenuTransactionType()

    object Income : StatisticsMenuTransactionType()

    override val menuType: StatisticsMenuType = StatisticsMenuType.TRANSACTION_TYPE
}

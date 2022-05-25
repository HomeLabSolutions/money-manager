package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuTransactionType : BaseStatisticsMenuType {

    @Parcelize
    object Expense : StatisticsMenuTransactionType()

    @Parcelize
    object Income : StatisticsMenuTransactionType()

    override val menuType: StatisticsMenuType = StatisticsMenuType.TRANSACTION_TYPE
}

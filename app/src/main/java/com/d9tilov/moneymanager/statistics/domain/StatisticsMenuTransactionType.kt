package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuTransactionType(val name: String) : BaseStatisticsMenuType {

    companion object {
        private const val STATISTICS_EXPENSE_NAME = "statistics_expense_name"
        private const val STATISTICS_INCOME_NAME = "statistics_income_name"
    }

    @Parcelize
    object EXPENSE : StatisticsMenuTransactionType(STATISTICS_EXPENSE_NAME)

    @Parcelize
    object INCOME : StatisticsMenuTransactionType(STATISTICS_INCOME_NAME)

    override val menuType: StatisticsMenuType = StatisticsMenuType.TRANSACTION_TYPE
}

package com.d9tilov.moneymanager.statistics.domain

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuCurrency(val name: String, val currencyCode: String) :
    BaseStatisticsMenuType {

    companion object {
        private const val STATISTICS_DEFAULT_CURRENCY_NAME = "statistics_default_currency_name"
        private const val STATISTICS_CURRENT_CURRENCY_NAME = "statistics_current_currency_name"
    }

    @Parcelize
    object DEFAULT : StatisticsMenuCurrency(STATISTICS_DEFAULT_CURRENCY_NAME, DEFAULT_CURRENCY_CODE)

    @Parcelize
    data class CURRENT(val code: String) :
        StatisticsMenuCurrency(STATISTICS_CURRENT_CURRENCY_NAME, code)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CURRENCY
}

package com.d9tilov.moneymanager.statistics.domain

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuCurrency(val currencyCode: String) : BaseStatisticsMenuType {

    @Parcelize
    object Default : StatisticsMenuCurrency(DEFAULT_CURRENCY_CODE)

    @Parcelize
    data class Current(val code: String) : StatisticsMenuCurrency(code)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CURRENCY
}

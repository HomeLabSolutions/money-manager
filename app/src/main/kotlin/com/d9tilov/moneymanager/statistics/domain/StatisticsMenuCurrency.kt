package com.d9tilov.moneymanager.statistics.domain

import com.d9tilov.android.core.constants.DataConstants.DEFAULT_CURRENCY_CODE

sealed class StatisticsMenuCurrency(val currencyCode: String) : BaseStatisticsMenuType {

    object Default : StatisticsMenuCurrency(DEFAULT_CURRENCY_CODE)

    data class Current(val code: String) : StatisticsMenuCurrency(code)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CURRENCY
}

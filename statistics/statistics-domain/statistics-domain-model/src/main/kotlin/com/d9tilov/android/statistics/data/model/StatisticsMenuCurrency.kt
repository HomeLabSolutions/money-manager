package com.d9tilov.android.statistics.data.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE

sealed class StatisticsMenuCurrency(
    val currencyCode: String,
) : BaseStatisticsMenuType {
    object Default : StatisticsMenuCurrency(DEFAULT_CURRENCY_CODE)

    data class Current(
        val code: String,
    ) : StatisticsMenuCurrency(code)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CURRENCY
}

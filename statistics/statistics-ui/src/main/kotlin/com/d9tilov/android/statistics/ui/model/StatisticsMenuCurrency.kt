package com.d9tilov.android.statistics.ui.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType

sealed class StatisticsMenuCurrency(
    val currencyCode: String,
) : BaseStatisticsMenuType {
    data object Default : StatisticsMenuCurrency(DEFAULT_CURRENCY_CODE)

    data class Current(
        val code: String,
    ) : StatisticsMenuCurrency(code)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CURRENCY
}

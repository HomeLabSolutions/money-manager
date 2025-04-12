package com.d9tilov.android.statistics.ui.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType

sealed class StatisticsMenuCurrencyType(
    val currencyCode: String,
) : BaseStatisticsMenuType {
    data object Default : StatisticsMenuCurrencyType(DEFAULT_CURRENCY_CODE)

    data class Current(
        val code: String,
    ) : StatisticsMenuCurrencyType(code)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CURRENCY
}

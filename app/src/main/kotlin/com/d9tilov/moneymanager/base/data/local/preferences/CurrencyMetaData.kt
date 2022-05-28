package com.d9tilov.moneymanager.base.data.local.preferences

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_SYMBOL

data class CurrencyMetaData(val code: String, val symbol: String) {

    companion object {
        val EMPTY = CurrencyMetaData(DEFAULT_CURRENCY_CODE, DEFAULT_CURRENCY_SYMBOL)
    }
}

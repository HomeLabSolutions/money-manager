package com.d9tilov.moneymanager.currency.data.entity

import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_CURRENCY_SYMBOL

data class CurrencyMetaData(val clientId: String, val code: String, val symbol: String) {

    companion object {
        val EMPTY = CurrencyMetaData(
            clientId = DataConstants.NO_ID.toString(),
            DEFAULT_CURRENCY_CODE,
            DEFAULT_CURRENCY_SYMBOL
        )
    }
}

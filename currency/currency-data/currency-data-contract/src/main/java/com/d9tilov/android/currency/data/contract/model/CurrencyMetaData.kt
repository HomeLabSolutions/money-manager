package com.d9tilov.android.currency.data.contract.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.constants.DataConstants

data class CurrencyMetaData(val clientId: String, val code: String, val symbol: String) {

    companion object {
        val EMPTY = CurrencyMetaData(
            clientId = DataConstants.NO_ID.toString(),
            DEFAULT_CURRENCY_CODE,
            DEFAULT_CURRENCY_SYMBOL
        )
    }
}

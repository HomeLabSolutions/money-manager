package com.d9tilov.android.currency.data.impl.api

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.currency.data.contract.model.CurrencyResponse
import com.d9tilov.android.currency.data.impl.BuildConfig

interface CurrencyApi {
    suspend fun getCurrencies(
        baseCurrency: String = DEFAULT_CURRENCY_CODE,
        key: String = BuildConfig.CURRENCY_API_KEY,
    ): CurrencyResponse
}

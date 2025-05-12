package com.d9tilov.android.currency.domain.contract

import com.d9tilov.android.currency.domain.model.Currency

interface GeocodingInteractor {
    suspend fun getCurrencyByCoords(
        latitude: Double,
        longitude: Double,
    ): Currency

    suspend fun updateLocalCurrency(currencyCode: String)

    suspend fun resetLocalCurrency()
}

package com.d9tilov.android.currency.domain.contract

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.currency.domain.model.Currency

interface GeocodingInteractor {
    suspend fun getCurrencyByCoords(locationData: LocationData): Currency

    suspend fun updateLocalCurrency(currencyCode: String)

    suspend fun resetLocalCurrency()
}

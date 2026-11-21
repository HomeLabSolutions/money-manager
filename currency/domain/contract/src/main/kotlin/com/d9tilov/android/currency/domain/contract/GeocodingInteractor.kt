package com.d9tilov.android.currency.domain.contract

import com.d9tilov.android.currency.domain.model.CurrencyRate
import com.d9tilov.android.places.domain.model.LocationData

interface GeocodingInteractor {
    suspend fun getCurrencyByCoords(locationData: LocationData): CurrencyRate

    suspend fun updateLocalCurrency(currencyCode: String)

    suspend fun resetLocalCurrency()
}

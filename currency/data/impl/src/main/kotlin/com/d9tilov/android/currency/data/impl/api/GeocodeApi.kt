package com.d9tilov.android.currency.data.impl.api

import com.d9tilov.android.currency.data.contract.model.GeocodingResponse
import com.d9tilov.android.currency.data.impl.BuildConfig

interface GeocodeApi {
    suspend fun getCurrencies(
        coords: String,
        key: String = BuildConfig.GEOCODING_API_KEY,
    ): GeocodingResponse
}

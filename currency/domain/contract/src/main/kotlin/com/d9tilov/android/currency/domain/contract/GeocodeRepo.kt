package com.d9tilov.android.currency.domain.contract

interface GeocodeRepo {

    suspend fun getCurrencyCodeByCoords(latitude: Double, longitude: Double): String
}

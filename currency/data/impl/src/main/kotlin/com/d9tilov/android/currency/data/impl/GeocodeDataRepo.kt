package com.d9tilov.android.currency.data.impl

import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import com.d9tilov.android.network.GeocodeApi

class GeocodeDataRepo(
    geocodeApi: GeocodeApi,

): GeocodeRepo {
    override suspend fun getCurrencyCodeByCoords(latitude: Double, longitude: Double): String {

    }
}
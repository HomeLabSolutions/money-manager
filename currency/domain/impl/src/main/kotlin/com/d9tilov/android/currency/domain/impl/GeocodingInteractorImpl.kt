package com.d9tilov.android.currency.domain.impl

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import com.d9tilov.android.currency.domain.contract.GeocodingInteractor
import com.d9tilov.android.currency.domain.model.Currency
import javax.inject.Inject

class GeocodingInteractorImpl @Inject constructor(
    val geocodingRepo: GeocodeRepo,
    val currencyInteractor: CurrencyInteractor
) : GeocodingInteractor {
    override suspend fun getCurrencyByCoords(latitude: Double, longitude: Double): Currency {
        val currencyCode = geocodingRepo.getCurrencyCodeByCoords(latitude, longitude)
        return currencyInteractor.getCurrencyByCode(currencyCode)
    }
}

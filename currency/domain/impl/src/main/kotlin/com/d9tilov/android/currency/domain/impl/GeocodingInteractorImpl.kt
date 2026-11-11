package com.d9tilov.android.currency.domain.impl

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import com.d9tilov.android.currency.domain.contract.GeocodingInteractor
import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.places.domain.model.LocationData
import dev.zacsweers.metro.Inject

@Inject
class GeocodingInteractorImpl(
    val geocodingRepo: GeocodeRepo,
    val currencyInteractor: CurrencyInteractor,
) : GeocodingInteractor {
    override suspend fun getCurrencyByCoords(locationData: LocationData): Currency {
        val currencyCode = geocodingRepo.getCurrencyCodeByCoords(locationData.latitude, locationData.longitude)
        return currencyInteractor.getCurrencyByCode(currencyCode)
    }

    override suspend fun updateLocalCurrency(currencyCode: String) = geocodingRepo.updateLocalCurrency(currencyCode)

    override suspend fun resetLocalCurrency() = geocodingRepo.resetLocalCurrency()
}

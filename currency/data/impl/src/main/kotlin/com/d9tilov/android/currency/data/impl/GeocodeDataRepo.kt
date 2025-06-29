package com.d9tilov.android.currency.data.impl

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.GeocodeApi
import javax.inject.Inject

class GeocodeDataRepo @Inject constructor(
    private val geocodeApi: GeocodeApi,
    private val preferencesStore: PreferencesStore,
) : GeocodeRepo {
    override suspend fun getCurrencyCodeByCoords(
        latitude: Double,
        longitude: Double,
    ): String =
        geocodeApi
            .getCurrencies(coords = "$latitude+$longitude")
            .results
            .firstOrNull()
            ?.annotations
            ?.currency
            ?.isoCode
            ?: DEFAULT_CURRENCY_CODE

    override suspend fun updateLocalCurrency(currencyCode: String) = preferencesStore.updateLocalCurrency(currencyCode)

    override suspend fun resetLocalCurrency() = preferencesStore.resetLocalCurrency()
}

package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.insights.domain.contract.InsightsConsentRepo
import javax.inject.Inject

class PreferencesInsightsConsentRepo @Inject constructor(
    private val preferencesStore: PreferencesStore,
) : InsightsConsentRepo {
    override val isGranted = preferencesStore.insightsConsentGranted

    override suspend fun grant() = preferencesStore.grantInsightsConsent()
}

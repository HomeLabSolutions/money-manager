package com.d9tilov.android.insights.data

import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import javax.inject.Inject

class PreferencesInsightsConsentRepository @Inject constructor(
    private val preferencesStore: PreferencesStore,
) : InsightsConsentRepository {
    override val isGranted = preferencesStore.insightsConsentGranted

    override suspend fun grant() = preferencesStore.grantInsightsConsent()

    override suspend fun revoke() = preferencesStore.revokeInsightsConsent()
}

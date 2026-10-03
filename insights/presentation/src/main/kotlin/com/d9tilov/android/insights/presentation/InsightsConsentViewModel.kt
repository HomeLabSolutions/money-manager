package com.d9tilov.android.insights.presentation

import androidx.lifecycle.ViewModel
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class InsightsConsentViewModel @Inject constructor(
    private val consentRepository: InsightsConsentRepository,
) : ViewModel() {
    suspend fun isConsentGranted(): Boolean = consentRepository.isGranted.first()

    suspend fun grantConsent() = consentRepository.grant()

    suspend fun revokeConsent() = consentRepository.revoke()
}

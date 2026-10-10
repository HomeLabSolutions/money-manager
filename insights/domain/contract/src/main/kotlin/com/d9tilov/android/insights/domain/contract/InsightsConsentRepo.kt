package com.d9tilov.android.insights.domain.contract

import kotlinx.coroutines.flow.Flow

interface InsightsConsentRepo {
    val isGranted: Flow<Boolean>

    suspend fun grant()
}

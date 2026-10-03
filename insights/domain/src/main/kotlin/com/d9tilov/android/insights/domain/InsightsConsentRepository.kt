package com.d9tilov.android.insights.domain

import kotlinx.coroutines.flow.Flow

interface InsightsConsentRepository {
    val isGranted: Flow<Boolean>

    suspend fun grant()

    suspend fun revoke()
}

package com.d9tilov.android.transaction.domain.contract

import com.d9tilov.android.transaction.domain.model.SpendingSummary
import kotlinx.coroutines.flow.Flow

interface ObserveSpendingSummaryUseCase {
    operator fun invoke(): Flow<SpendingSummary>
}

package com.d9tilov.android.widget.domain.model

import com.d9tilov.android.transaction.domain.model.SpendingSummary

sealed interface SpendingWidgetState {
    data object SignedOut : SpendingWidgetState

    data object SetupRequired : SpendingWidgetState

    data object PremiumRequired : SpendingWidgetState

    data object Unavailable : SpendingWidgetState

    data class Ready(
        val currencyCode: String,
        val spendingSummary: SpendingSummary,
    ) : SpendingWidgetState
}

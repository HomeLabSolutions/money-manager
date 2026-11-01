package com.d9tilov.moneymanager.widget

import java.math.BigDecimal

sealed interface SpendingWidgetState {
    data object SignedOut : SpendingWidgetState

    data object SetupRequired : SpendingWidgetState

    data object PremiumRequired : SpendingWidgetState

    data object Unavailable : SpendingWidgetState

    data class Ready(
        val currencyCode: String,
        val availableToday: BigDecimal,
        val spentToday: BigDecimal,
        val spentInPeriod: BigDecimal,
    ) : SpendingWidgetState
}

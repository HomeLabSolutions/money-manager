package com.d9tilov.android.transaction.domain.model

import java.math.BigDecimal

sealed interface TransactionSpendingTodayModel {
    data class OVERSPENDING(
        val trSum: BigDecimal,
    ) : TransactionSpendingTodayModel

    data class NORMAL(
        val trSum: BigDecimal,
    ) : TransactionSpendingTodayModel
}

package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.core.model.Decimal

sealed interface TransactionSpendingTodayModel {
    data class OVERSPENDING(
        val trSum: Decimal,
    ) : TransactionSpendingTodayModel

    data class NORMAL(
        val trSum: Decimal,
    ) : TransactionSpendingTodayModel
}

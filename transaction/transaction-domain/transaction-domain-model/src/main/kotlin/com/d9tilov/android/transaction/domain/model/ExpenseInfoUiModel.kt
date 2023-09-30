package com.d9tilov.android.transaction.domain.model

import java.math.BigDecimal

data class ExpenseInfoUiModel(
    val ableToSpendTodayModel: TransactionSpendingTodayModel,
    val isSpendTodayApprox: Boolean,
    val spentTodaySumApprox: BigDecimal,
    val isSpendInPeriodApprox: Boolean,
    val spentInPeriodSumApprox: BigDecimal
)

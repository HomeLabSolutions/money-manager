package com.d9tilov.android.transaction.domain.model

import java.math.BigDecimal

data class ExpenseInfoUiModel(
    val ableToSpendTodayModel: TransactionSpendingTodayModel,
    val spentTodaySum: BigDecimal,
    val spentTodaySumApprox: BigDecimal,
    val spentInPeriodSum: BigDecimal,
    val spentInPeriodSumApprox: BigDecimal
)

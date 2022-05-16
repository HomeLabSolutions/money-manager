package com.d9tilov.moneymanager.transaction.domain.entity

import java.math.BigDecimal

data class ExpenseInfoUiModel(
    val ableToSpendTodayModel: TransactionSpendingTodayModel,
    val spentTodaySum: BigDecimal,
    val spentTodaySumApprox: BigDecimal,
    val spentInPeriodSum: BigDecimal,
    val spentInPeriodSumApprox: BigDecimal
)

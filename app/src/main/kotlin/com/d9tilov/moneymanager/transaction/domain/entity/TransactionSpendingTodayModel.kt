package com.d9tilov.moneymanager.transaction.domain.entity

import java.math.BigDecimal

sealed class TransactionSpendingTodayModel {

    data class OVERSPENDING(val trSum: BigDecimal) : TransactionSpendingTodayModel()

    data class NORMAL(val trSum: BigDecimal) : TransactionSpendingTodayModel()
}

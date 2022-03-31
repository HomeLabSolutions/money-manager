package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

sealed class TransactionSpendingTodayModel(sum: BigDecimal) : Parcelable {

    @Parcelize
    data class OVERSPENDING(val trSum: BigDecimal) : TransactionSpendingTodayModel(trSum)

    @Parcelize
    data class NORMAL(val trSum: BigDecimal) : TransactionSpendingTodayModel(trSum)
}

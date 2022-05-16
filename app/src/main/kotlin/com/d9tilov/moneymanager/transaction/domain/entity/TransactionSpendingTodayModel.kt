package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

sealed class TransactionSpendingTodayModel : Parcelable {

    @Parcelize
    data class OVERSPENDING(val trSum: BigDecimal) : TransactionSpendingTodayModel()

    @Parcelize
    data class NORMAL(val trSum: BigDecimal) : TransactionSpendingTodayModel()
}

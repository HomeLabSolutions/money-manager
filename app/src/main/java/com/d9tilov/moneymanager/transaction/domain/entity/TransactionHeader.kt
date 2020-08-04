package com.d9tilov.moneymanager.transaction.domain.entity

import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.HEADER
import java.util.Date

data class TransactionHeader(
    val date: Date,
    override val headerPosition: Int = 0
) : BaseTransaction {
    override val itemType: Int
        get() = HEADER
}
package com.d9tilov.moneymanager.transaction.domain.entity

import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.HEADER
import kotlinx.datetime.LocalDateTime

data class TransactionHeader(
    val date: LocalDateTime,
    val currency: String,
    override val headerPosition: Int = 0
) : BaseTransaction {
    override val itemType: Int
        get() = HEADER
}

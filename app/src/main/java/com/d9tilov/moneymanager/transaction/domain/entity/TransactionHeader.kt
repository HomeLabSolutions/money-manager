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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransactionHeader) return false

        if (date != other.date) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + currency.hashCode()
        return result
    }
}

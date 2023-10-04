package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.transaction.domain.model.BaseTransaction.Companion.HEADER
import kotlinx.datetime.LocalDateTime

data class TransactionHeader(
    override val date: LocalDateTime,
    val currency: String,
) : BaseTransaction {
    override val itemType: Int = HEADER
}

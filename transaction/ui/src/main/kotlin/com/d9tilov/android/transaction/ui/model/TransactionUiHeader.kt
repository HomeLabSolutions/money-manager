package com.d9tilov.android.transaction.ui.model

import com.d9tilov.android.transaction.ui.model.BaseTransaction.Companion.HEADER
import kotlinx.datetime.LocalDateTime

data class TransactionUiHeader(
    override val date: LocalDateTime,
    val currency: String,
) : BaseTransaction {
    override val itemType: Int = HEADER
}

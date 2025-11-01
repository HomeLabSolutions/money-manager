package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.core.utils.currentDateTime
import kotlinx.datetime.LocalDateTime

data class TransactionMinMaxDateModel(
    val minDate: LocalDateTime,
    val maxDate: LocalDateTime,
) {
    companion object {
        val EMPTY = TransactionMinMaxDateModel(minDate = currentDateTime(), maxDate = currentDateTime())
    }
}

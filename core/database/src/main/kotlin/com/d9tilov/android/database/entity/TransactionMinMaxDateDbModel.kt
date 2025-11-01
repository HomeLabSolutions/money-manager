package com.d9tilov.android.database.entity

import kotlinx.datetime.LocalDateTime

data class TransactionMinMaxDateDbModel(
    val minDate: LocalDateTime?,
    val maxDate: LocalDateTime?,
)

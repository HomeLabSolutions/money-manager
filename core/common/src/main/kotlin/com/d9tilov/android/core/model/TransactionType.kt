package com.d9tilov.android.core.model

sealed class TransactionType(
    open val value: Int,
    val name: String,
) {
    data object INCOME : TransactionType(0, "income")

    data object EXPENSE : TransactionType(1, "expense")
}

fun TransactionType.isIncome() = this is TransactionType.INCOME

fun Int.toType() =
    when (this) {
        0 -> TransactionType.INCOME
        1 -> TransactionType.EXPENSE
        else -> throw IllegalArgumentException("Wrong transaction type: $this")
    }

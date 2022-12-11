package com.d9tilov.android.core.model

sealed class TransactionType(open val name: String) {

    companion object {
        const val INCOME_TRANSACTION_NAME = "Income"
        const val EXPENSE_TRANSACTION_NAME = "Expense"
    }

    object INCOME : TransactionType(INCOME_TRANSACTION_NAME)

    object EXPENSE : TransactionType(EXPENSE_TRANSACTION_NAME)
}

fun TransactionType.isIncome() = this is TransactionType.INCOME

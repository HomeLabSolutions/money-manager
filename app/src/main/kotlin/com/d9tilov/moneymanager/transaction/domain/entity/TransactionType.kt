package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import kotlinx.parcelize.Parcelize

sealed class TransactionType(open val name: String) : Parcelable {

    companion object {
        const val INCOME_TRANSACTION_NAME = "Income"
        const val EXPENSE_TRANSACTION_NAME = "Expense"

        @JsonCreator
        @JvmStatic
        private fun creator(name: String): TransactionType? {
            return TransactionType::class.sealedSubclasses.firstOrNull { it.simpleName == name }?.objectInstance
        }
    }

    @Parcelize
    object INCOME : TransactionType(INCOME_TRANSACTION_NAME)

    @Parcelize
    object EXPENSE : TransactionType(EXPENSE_TRANSACTION_NAME)

}

fun TransactionType.isIncome() = this is TransactionType.INCOME

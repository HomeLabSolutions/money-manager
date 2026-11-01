package com.d9tilov.android.transaction.domain.contract

fun interface TransactionChangeNotifier {
    suspend fun onTransactionChanged()
}

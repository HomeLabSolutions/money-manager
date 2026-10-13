package com.d9tilov.android.incomeexpense.ui

import androidx.paging.PagingData
import androidx.paging.filter
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal fun Flow<PagingData<TransactionUiModel>>.searchTransactions(
    queries: Flow<String>,
): Flow<PagingData<TransactionUiModel>> =
    combine(this, queries.map { it.trim() }.distinctUntilChanged()) { data, query ->
        if (query.isEmpty()) data else data.filter { transaction -> transaction.matchesSearch(query) }
    }

internal fun TransactionUiModel.matchesSearch(query: String): Boolean {
    val text = query.trim()
    val amountQuery = text.replace(',', '.').filterNot { it.isWhitespace() }
    val amountMatches = amountQuery.isNotEmpty() && sum.toPlainString().contains(amountQuery)
    return text.isEmpty() ||
        category.name.contains(text, ignoreCase = true) ||
        description.contains(text, ignoreCase = true) ||
        amountMatches
}

package com.d9tilov.android.incomeexpense.ui.vm

data class TransactionSearchState(
    val searchQuery: String = "",
    val status: SearchStatus = SearchStatus.CLOSED,
)

enum class SearchStatus {
    CLOSED,
    OPEN,
}

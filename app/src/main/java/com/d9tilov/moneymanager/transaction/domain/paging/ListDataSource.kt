package com.d9tilov.moneymanager.transaction.domain.paging

import androidx.paging.PositionalDataSource
import kotlin.math.min

class ListDataSource<T>(private val items: List<T>) : PositionalDataSource<T>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        callback.onResult(items, 0, items.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        val start = params.startPosition
        val end = params.startPosition + params.loadSize
        val minEnd = min(items.size, end)
        callback.onResult(items.subList(start, minEnd))
    }
}

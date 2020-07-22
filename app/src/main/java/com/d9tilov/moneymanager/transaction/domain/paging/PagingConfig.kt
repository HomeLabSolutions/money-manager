package com.d9tilov.moneymanager.transaction.domain.paging

import androidx.paging.PagedList

object PagingConfig {

    private const val PAGE_SIZE = 30
    private const val INITIAL_PAGE_SIZE = 3 * PAGE_SIZE

    fun createConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(INITIAL_PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(false)
        .build()
}

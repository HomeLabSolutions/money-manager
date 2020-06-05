package com.d9tilov.moneymanager.core.util.events

interface OnItemLongClickListener<in R> {
    fun onItemLongClick(item: R, position: Int = 0)
}

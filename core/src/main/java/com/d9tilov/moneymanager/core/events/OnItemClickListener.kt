package com.d9tilov.moneymanager.core.events

interface OnItemClickListener<in R> {
    fun onItemClick(item: R, position: Int = 0)
}

package com.d9tilov.moneymanager.core.events

fun interface OnItemClickListener<in R> {
    fun onItemClick(item: R, position: Int)
}

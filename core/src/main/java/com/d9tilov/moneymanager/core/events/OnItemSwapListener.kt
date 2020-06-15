package com.d9tilov.moneymanager.core.events

interface OnItemSwapListener<in R> {
    fun onItemSwap(items: List<R>)
}

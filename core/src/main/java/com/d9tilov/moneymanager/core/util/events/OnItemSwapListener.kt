package com.d9tilov.moneymanager.core.util.events

interface OnItemSwapListener<in R> {
    fun onItemSwap(items: List<R>)
}

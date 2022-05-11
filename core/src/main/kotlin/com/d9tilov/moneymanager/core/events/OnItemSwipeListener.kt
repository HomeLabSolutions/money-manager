package com.d9tilov.moneymanager.core.events

interface OnItemSwipeListener<in R> {
    fun onItemSwiped(item: R, position: Int = 0)
}

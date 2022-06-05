package com.d9tilov.moneymanager.core.events

fun interface OnItemSwipeListener<in R> {
    fun onItemSwiped(item: R, position: Int)
}

package com.d9tilov.android.core.events

fun interface OnItemSwipeListener<in R> {
    fun onItemSwiped(item: R, position: Int)
}

package com.d9tilov.android.core.events

fun interface OnItemClickListener<in R> {
    fun onItemClick(
        item: R,
        position: Int,
    )
}

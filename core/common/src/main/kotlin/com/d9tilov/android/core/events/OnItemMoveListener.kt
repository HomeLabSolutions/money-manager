package com.d9tilov.android.core.events

interface OnItemMoveListener<in R> {

    fun onItemAddToFolder(
        childItem: R,
        childPosition: Int = 0,
        parentItem: R,
        parentPosition: Int = 0
    )

    fun onItemsUnitToFolder(
        firstItem: R,
        firstChildPosition: Int = 0,
        secondItem: R,
        secondItemPosition: Int = 0
    )
}

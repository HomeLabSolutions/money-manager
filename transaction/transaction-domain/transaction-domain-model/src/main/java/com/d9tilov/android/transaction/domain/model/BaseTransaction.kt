package com.d9tilov.android.transaction.domain.model

interface BaseTransaction {

    val itemType: Int
    val headerPosition: Int

    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}

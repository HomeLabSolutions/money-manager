package com.d9tilov.android.transaction.ui.model

import kotlinx.datetime.LocalDateTime

interface BaseTransaction {
    val itemType: Int
    val date: LocalDateTime

    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}

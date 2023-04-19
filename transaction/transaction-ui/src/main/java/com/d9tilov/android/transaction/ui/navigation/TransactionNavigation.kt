package com.d9tilov.android.transaction.ui.navigation

import com.d9tilov.android.common_android.ui.base.BaseNavigator

interface RemoveTransactionDialogNavigator : BaseNavigator {
    fun remove()
}

interface EditTransactionNavigator : BaseNavigator {
    fun save()
}

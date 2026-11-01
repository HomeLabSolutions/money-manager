package com.d9tilov.android.widget.ui

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.d9tilov.android.transaction.domain.contract.TransactionChangeNotifier
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpendingWidgetTransactionChangeNotifier @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : TransactionChangeNotifier {
    override suspend fun onTransactionChanged() {
        SpendingWidget().updateAll(context)
    }
}

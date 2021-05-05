package com.d9tilov.moneymanager.periodic.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction

class PeriodicTransactionDiffUtil(
    private val oldPeriodicTransactions: List<PeriodicTransaction>,
    private val newPeriodicTransactions: List<PeriodicTransaction>,
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPeriodicTransactions[oldItemPosition].id == newPeriodicTransactions[newItemPosition].id

    override fun getOldListSize() = oldPeriodicTransactions.size
    override fun getNewListSize() = newPeriodicTransactions.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPeriodicTransactions[oldItemPosition] == newPeriodicTransactions[newItemPosition]
}

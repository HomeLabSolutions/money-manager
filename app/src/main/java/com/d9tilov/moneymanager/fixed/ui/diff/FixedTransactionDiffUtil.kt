package com.d9tilov.moneymanager.fixed.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction

class FixedTransactionDiffUtil(
    private val oldFixedTransactions: List<FixedTransaction>,
    private val newFixedTransactions: List<FixedTransaction>,
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldFixedTransactions[oldItemPosition].id == newFixedTransactions[newItemPosition].id

    override fun getOldListSize() = oldFixedTransactions.size
    override fun getNewListSize() = newFixedTransactions.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldFixedTransactions[oldItemPosition] == newFixedTransactions[newItemPosition]
}

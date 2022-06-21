package com.d9tilov.moneymanager.regular.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction

class RegularTransactionDiffUtil(
    private val oldRegularTransactions: List<RegularTransaction>,
    private val newRegularTransactions: List<RegularTransaction>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldRegularTransactions[oldItemPosition].id == newRegularTransactions[newItemPosition].id

    override fun getOldListSize() = oldRegularTransactions.size
    override fun getNewListSize() = newRegularTransactions.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldRegularTransactions[oldItemPosition] == newRegularTransactions[newItemPosition]
}

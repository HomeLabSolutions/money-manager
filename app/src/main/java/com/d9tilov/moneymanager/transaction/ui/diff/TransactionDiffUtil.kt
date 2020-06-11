package com.d9tilov.moneymanager.transaction.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

class TransactionDiffUtil(
    private val oldTransactionList: List<Transaction>,
    private val newTransactionList: List<Transaction>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldTransactionList[oldItemPosition].id == newTransactionList[newItemPosition].id

    override fun getOldListSize() = oldTransactionList.size
    override fun getNewListSize() = newTransactionList.size
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) =
        newTransactionList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldTransactionList[oldItemPosition] == newTransactionList[newItemPosition]
}

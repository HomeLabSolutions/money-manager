package com.d9tilov.moneymanager.statistics.ui.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

class StatisticsDetailsDiffUtils(
    private val oldData: List<Transaction>,
    private val newData: List<Transaction>,
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition].id == newData[newItemPosition].id

    override fun getOldListSize() = oldData.size
    override fun getNewListSize() = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition] == newData[newItemPosition]
}

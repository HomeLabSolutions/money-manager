package com.d9tilov.android.statistics.ui.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.android.transaction.domain.model.TransactionChartModel

class StatisticsBarCharDiffUtils(
    private val oldData: List<TransactionChartModel>,
    private val newData: List<TransactionChartModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition].category.id == newData[newItemPosition].category.id

    override fun getOldListSize() = oldData.size
    override fun getNewListSize() = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition] == newData[newItemPosition]
}

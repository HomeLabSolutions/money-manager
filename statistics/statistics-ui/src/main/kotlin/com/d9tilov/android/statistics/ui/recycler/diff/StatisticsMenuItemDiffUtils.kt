package com.d9tilov.android.statistics.ui.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType

class StatisticsMenuItemDiffUtils(
    private val oldData: List<com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType>,
    private val newData: List<com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition].menuType == newData[newItemPosition].menuType

    override fun getOldListSize() = oldData.size
    override fun getNewListSize() = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition] == newData[newItemPosition]
}

package com.d9tilov.moneymanager.statistics.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.statistics.domain.BaseStatisticsMenuType

class StatisticsMenuItemDiffUtils(
    private val oldData: List<BaseStatisticsMenuType>,
    private val newData: List<BaseStatisticsMenuType>,
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition].menuType == newData[newItemPosition].menuType

    override fun getOldListSize() = oldData.size
    override fun getNewListSize() = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition] == newData[newItemPosition]
}

package com.d9tilov.moneymanager.goal.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.goal.domain.entity.Goal

class GoalDiffUtil(
    private val oldGoalData: List<Goal>,
    private val newGoalData: List<Goal>,
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldGoalData[oldItemPosition].id == newGoalData[newItemPosition].id

    override fun getOldListSize() = oldGoalData.size
    override fun getNewListSize() = newGoalData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldGoalData[oldItemPosition] == newGoalData[newItemPosition]
}

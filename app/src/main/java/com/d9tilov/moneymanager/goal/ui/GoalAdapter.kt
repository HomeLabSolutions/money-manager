package com.d9tilov.moneymanager.goal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getCurrencySignBy
import com.d9tilov.moneymanager.databinding.ItemGoalBinding
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import com.d9tilov.moneymanager.goal.ui.diff.GoalDiffUtil

class GoalAdapter : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    private var goals = mutableListOf<Goal>()
    var itemClickListener: OnItemClickListener<Goal>? = null
    var itemSwipeListener: OnItemSwipeListener<Goal>? = null
    private var removedItemPosition: Int = 0

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val viewBinding =
            ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder =
            GoalViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(goals[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(goals[position], position)
    }

    override fun getItemCount() = goals.size

    override fun getItemId(position: Int) = goals[position].id

    fun updateItems(newGoalData: List<Goal>) {
        val diffUtilsCallback =
            GoalDiffUtil(
                goals,
                newGoalData
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        goals.clear()
        goals.addAll(newGoalData)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun deleteItem(position: Int) {
        removedItemPosition = position
        val goalToDelete = goals[position]
        itemSwipeListener?.onItemSwiped(goalToDelete, position)
    }

    fun cancelDeletion() {
        notifyItemChanged(removedItemPosition)
    }

    class GoalViewHolder(private val viewBinding: ItemGoalBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(goal: Goal, position: Int) {
            viewBinding.itemGoalTitle.text = goal.name
            viewBinding.itemGoalProgress.setProgress(
                goal.currentSum.toInt(),
                goal.targetSum.toInt(),
                getCurrencySignBy(goal.currencyCode),
                DELAY_ANIM_MULTIPLIER * (position + 1)
            )
        }

        companion object {
            private const val DELAY_ANIM_MULTIPLIER = 0L
        }
    }
}

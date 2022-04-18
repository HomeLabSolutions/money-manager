package com.d9tilov.moneymanager.regular.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemRegularTransactionBinding
import com.d9tilov.moneymanager.regular.domain.entity.ExecutionPeriod
import com.d9tilov.moneymanager.regular.domain.entity.PeriodType
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.regular.ui.diff.RegularTransactionDiffUtil

class RegularTransactionAdapter :
    RecyclerView.Adapter<RegularTransactionAdapter.RegularTransactionViewHolder>() {

    private var regularTransactions = mutableListOf<RegularTransaction>()
    var itemClickListener: OnItemClickListener<RegularTransaction>? = null
    var checkBoxClickListener: OnItemClickListener<RegularTransaction>? = null
    var itemSwipeListener: OnItemSwipeListener<RegularTransaction>? = null
    private var removedItemPosition: Int = 0

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegularTransactionViewHolder {
        val viewBinding =
            ItemRegularTransactionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        val viewHolder =
            RegularTransactionViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(
                    regularTransactions[adapterPosition],
                    adapterPosition
                )
            }
        }
        viewBinding.itemRegularTransactionNotify.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                checkBoxClickListener?.onItemClick(
                    regularTransactions[adapterPosition],
                    adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RegularTransactionViewHolder, position: Int) {
        holder.bind(regularTransactions[position])
    }

    override fun getItemCount() = regularTransactions.size

    override fun getItemId(position: Int) = regularTransactions[position].id

    fun updateItems(newRegularTransactions: List<RegularTransaction>) {
        val diffUtilsCallback =
            RegularTransactionDiffUtil(
                regularTransactions,
                newRegularTransactions
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        regularTransactions.clear()
        regularTransactions.addAll(newRegularTransactions)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun deleteItem(position: Int) {
        removedItemPosition = position
        val transactionToDelete = regularTransactions[position]
        itemSwipeListener?.onItemSwiped(transactionToDelete, position)
    }

    fun cancelDeletion() {
        notifyItemChanged(removedItemPosition)
    }

    class RegularTransactionViewHolder(private val viewBinding: ItemRegularTransactionBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(regularTransaction: RegularTransaction) {
            viewBinding.run {
                itemRegularTransactionSum.setValue(regularTransaction.sum, regularTransaction.currencyCode)
                itemRegularTransactionCategoryName.text = regularTransaction.category.name
                itemRegularTransactionCategoryName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        regularTransaction.category.color
                    )
                )
                val drawable = createTintDrawable(
                    context,
                    regularTransaction.category.icon,
                    regularTransaction.category.color
                )
                GlideApp
                    .with(context)
                    .load(drawable)
                    .into(itemRegularTransactionIcon)
                itemRegularTransactionDescription.text = regularTransaction.description
                itemRegularTransactionNotify.isChecked = regularTransaction.pushEnabled
                itemRegularTransactionDate.text =
                    when (regularTransaction.executionPeriod.periodType) {
                        PeriodType.DAY -> context.getString(R.string.regular_transaction_repeat_period_day)
                        PeriodType.WEEK -> context.getString(
                            R.string.regular_transaction_repeat_period_week,
                            getWeekDayString((regularTransaction.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek)
                        )
                        PeriodType.MONTH -> {
                            val dayOfMonth =
                                (regularTransaction.executionPeriod as ExecutionPeriod.EveryMonth).dayOfMonth
                            context.getString(
                                R.string.regular_transaction_repeat_period_month,
                                dayOfMonth.toString()
                            )
                        }
                    }
            }
        }

        private fun getWeekDayString(day: Int) = when (day) {
            0 -> context.getString(R.string.regular_transaction_repeat_monday)
            1 -> context.getString(R.string.regular_transaction_repeat_tuesday)
            2 -> context.getString(R.string.regular_transaction_repeat_wednesday)
            3 -> context.getString(R.string.regular_transaction_repeat_thursday)
            4 -> context.getString(R.string.regular_transaction_repeat_friday)
            5 -> context.getString(R.string.regular_transaction_repeat_saturday)
            6 -> context.getString(R.string.regular_transaction_repeat_sunday)
            else -> throw IllegalArgumentException("Unknown day of week: $day")
        }
    }
}

package com.d9tilov.moneymanager.regular.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d9tilov.moneymanager.R
import com.d9tilov.android.core.events.OnItemClickListener
import com.d9tilov.android.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.databinding.ItemRegularTransactionBinding
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.PeriodType
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.regular.domain.entity.WeekDays
import com.d9tilov.moneymanager.regular.ui.diff.RegularTransactionDiffUtil

class RegularTransactionAdapter(
    private val itemClickListener: OnItemClickListener<RegularTransaction>,
    private val checkBoxClickListener: OnItemClickListener<RegularTransaction>,
    private val itemSwipeListener: OnItemSwipeListener<RegularTransaction>
) :
    RecyclerView.Adapter<RegularTransactionAdapter.RegularTransactionViewHolder>() {

    private var regularTransactions = mutableListOf<RegularTransaction>()
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
                itemClickListener.onItemClick(regularTransactions[adapterPosition], adapterPosition)
            }
        }
        viewBinding.itemRegularTransactionNotify.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                checkBoxClickListener.onItemClick(regularTransactions[adapterPosition], adapterPosition)
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
        itemSwipeListener.onItemSwiped(transactionToDelete, position)
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
                val drawable = createTintDrawable(
                    context,
                    regularTransaction.category.icon,
                    regularTransaction.category.color
                )
                Glide
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
            WeekDays.MONDAY.ordinal -> context.getString(R.string.regular_transaction_repeat_monday)
            WeekDays.TUESDAY.ordinal -> context.getString(R.string.regular_transaction_repeat_tuesday)
            WeekDays.WEDNESDAY.ordinal -> context.getString(R.string.regular_transaction_repeat_wednesday)
            WeekDays.THURSDAY.ordinal -> context.getString(R.string.regular_transaction_repeat_thursday)
            WeekDays.FRIDAY.ordinal -> context.getString(R.string.regular_transaction_repeat_friday)
            WeekDays.SATURDAY.ordinal -> context.getString(R.string.regular_transaction_repeat_saturday)
            WeekDays.SUNDAY.ordinal -> context.getString(R.string.regular_transaction_repeat_sunday)
            else -> throw IllegalArgumentException("Unknown day of week: $day")
        }
    }
}

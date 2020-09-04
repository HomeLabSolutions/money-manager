package com.d9tilov.moneymanager.fixed.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.TRANSACTION_DATE_FORMAT_DAY_MONTH
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemFixedTransactionBinding
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.fixed.ui.diff.FixedTransactionDiffUtil
import com.d9tilov.moneymanager.period.PeriodType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FixedTransactionAdapter :
    RecyclerView.Adapter<FixedTransactionAdapter.FixedTransactionViewHolder>() {

    private var fixedTransactions = mutableListOf<FixedTransaction>()
    var itemClickListener: OnItemClickListener<FixedTransaction>? = null
    var checkBoxClickListener: OnItemClickListener<FixedTransaction>? = null
    var itemSwipeListener: OnItemSwipeListener<FixedTransaction>? = null
    private var removedItemPosition: Int = 0

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixedTransactionViewHolder {
        val viewBinding =
            ItemFixedTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder =
            FixedTransactionViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(fixedTransactions[adapterPosition], adapterPosition)
            }
        }
        viewBinding.itemFixedTransactionNotify.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                checkBoxClickListener?.onItemClick(
                    fixedTransactions[adapterPosition],
                    adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FixedTransactionViewHolder, position: Int) {
        holder.bind(fixedTransactions[position])
    }

    override fun getItemCount() = fixedTransactions.size

    override fun getItemId(position: Int) = fixedTransactions[position].id

    fun updateItems(newFixedTransactions: List<FixedTransaction>) {
        val diffUtilsCallback =
            FixedTransactionDiffUtil(
                fixedTransactions,
                newFixedTransactions
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        fixedTransactions.clear()
        fixedTransactions.addAll(newFixedTransactions)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun deleteItem(position: Int) {
        removedItemPosition = position
        val transactionToDelete = fixedTransactions[position]
        itemSwipeListener?.onItemSwiped(transactionToDelete, position)
    }

    fun cancelDeletion() {
        notifyItemChanged(removedItemPosition)
    }

    class FixedTransactionViewHolder(private val viewBinding: ItemFixedTransactionBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(fixedTransaction: FixedTransaction) {
            viewBinding.run {
                itemFixedTransactionSum.setValue(fixedTransaction.sum)
                itemFixedTransactionCategoryName.text = fixedTransaction.category.name
                itemFixedTransactionCategoryName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        fixedTransaction.category.color
                    )
                )
                val drawable = createTintDrawable(
                    context,
                    fixedTransaction.category.icon,
                    fixedTransaction.category.color
                )
                GlideApp
                    .with(context)
                    .load(drawable)
                    .into(itemFixedTransactionIcon)
                itemFixedTransactionDescription.text = fixedTransaction.description
                itemFixedTransactionNotify.isChecked = fixedTransaction.pushEnable
                itemFixedTransactionDate.text = when (fixedTransaction.periodType) {
                    PeriodType.DAY -> context.getString(R.string.fixed_transaction_repeat_period_day)
                    PeriodType.WEEK -> context.getString(
                        R.string.fixed_transaction_repeat_period_week,
                        getWeekDays(fixedTransaction.dayOfWeek)
                    )
                    PeriodType.MONTH -> {
                        val calendar = Calendar.getInstance()
                        calendar.time = fixedTransaction.startDate
                        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                        context.getString(
                            R.string.fixed_transaction_repeat_period_month, dayOfMonth.toString()
                        )
                    }
                    PeriodType.YEAR -> context.getString(
                        R.string.fixed_transaction_repeat_period_year,
                        SimpleDateFormat(
                            TRANSACTION_DATE_FORMAT_DAY_MONTH,
                            Locale.getDefault()
                        ).format(fixedTransaction.startDate)
                    )
                }
            }
        }

        private fun getWeekDays(days: Int): String {
            val weekDays = mutableListOf<String>()
            for (i in 0..6) {
                if ((days shr i) and 1 == 1) {
                    val dayOfWeek = when (i) {
                        0 -> context.getString(R.string.fixed_transaction_repeat_sunday)
                        1 -> context.getString(R.string.fixed_transaction_repeat_monday)
                        2 -> context.getString(R.string.fixed_transaction_repeat_tuesday)
                        3 -> context.getString(R.string.fixed_transaction_repeat_wednesday)
                        4 -> context.getString(R.string.fixed_transaction_repeat_thursday)
                        5 -> context.getString(R.string.fixed_transaction_repeat_friday)
                        6 -> context.getString(R.string.fixed_transaction_repeat_saturday)
                        else -> throw IllegalArgumentException("Unknown day of week: $i")
                    }
                    weekDays.add(dayOfWeek)
                }
            }
            return weekDays.joinToString(separator = ", ")
        }
    }
}

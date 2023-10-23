package com.d9tilov.android.statistics.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.android.common.android.ui.base.BaseViewHolder
import com.d9tilov.android.common.android.utils.DATE_FORMAT
import com.d9tilov.android.common.android.utils.createTintDrawable
import com.d9tilov.android.common.android.utils.gone
import com.d9tilov.android.common.android.utils.hide
import com.d9tilov.android.common.android.utils.let2
import com.d9tilov.android.common.android.utils.show
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.statistics.ui.recycler.diff.StatisticsDetailsDiffUtils
import com.d9tilov.android.statistics_ui.R
import com.d9tilov.android.statistics_ui.databinding.ItemTransactionDetailsBinding
import com.d9tilov.android.transaction.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.Locale

class StatisticsDetailsAdapter :
    RecyclerView.Adapter<StatisticsDetailsAdapter.TransactionDetailsViewHolder>() {

    private val transactions = mutableListOf<Transaction>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionDetailsViewHolder {
        val viewBinding = ItemTransactionDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionDetailsViewHolder(viewBinding)
    }

    override fun onBindViewHolder(
        holder: TransactionDetailsViewHolder,
        position: Int
    ) {
        holder.bind(transactions[position])
    }

    fun updateItems(newData: List<Transaction>) {
        val diffUtilsCallback = StatisticsDetailsDiffUtils(transactions, newData)
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        transactions.clear()
        transactions.addAll(newData)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = transactions.size

    override fun getItemId(position: Int) = transactions[position].id

    class TransactionDetailsViewHolder(private val viewBinding: ItemTransactionDetailsBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(transaction: Transaction) {
            viewBinding.run {
                itemTransactionCategory.text = transaction.category.name
                transaction.category.color?.let { color ->
                    itemTransactionCategory.setTextColor(ContextCompat.getColor(context, color))
                }
                if (transaction.isRegular) itemTransactionRegularIcon.show() else itemTransactionRegularIcon.hide()
                if (!transaction.inStatistics) {
                    itemTransactionStatisticsIcon.show()
                } else {
                    itemTransactionStatisticsIcon.hide()
                }
                val description = transaction.description
                itemTransactionDescription.text = transaction.description
                if (description.isNotEmpty()) {
                    itemTransactionDescription.show()
                } else {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(viewBinding.root)
                    constraintSet.connect(
                        R.id.item_transaction_category,
                        ConstraintSet.TOP,
                        R.id.item_transaction_icon,
                        ConstraintSet.TOP,
                        0
                    )
                    constraintSet.connect(
                        R.id.item_transaction_category,
                        ConstraintSet.BOTTOM,
                        R.id.item_transaction_icon,
                        ConstraintSet.BOTTOM,
                        0
                    )
                    constraintSet.applyTo(viewBinding.root)
                }
                itemTransactionSum.setValue(transaction.sum, transaction.currencyCode)
                itemTransactionUsdSum.setValue(
                    transaction.usdSum,
                    DEFAULT_CURRENCY_CODE
                )
                if (transaction.currencyCode != DEFAULT_CURRENCY_CODE) {
                    itemTransactionUsdSum.show()
                } else {
                    itemTransactionUsdSum.gone()
                }
                val drawable = let2(transaction.category.icon, transaction.category.color) { icon, color ->
                    createTintDrawable(context, icon, color)
                }
                itemTransactionDate.text = SimpleDateFormat(
                    DATE_FORMAT,
                    Locale.getDefault()
                ).format(transaction.date.toMillis())
                Glide.with(context).load(drawable).apply(
                    RequestOptions().override(
                        IMAGE_SIZE_IN_PX,
                        IMAGE_SIZE_IN_PX
                    )
                ).into(itemTransactionIcon)
            }
        }

        companion object {
            private const val IMAGE_SIZE_IN_PX = 136
        }
    }
}
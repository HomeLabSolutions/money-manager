package com.d9tilov.moneymanager.transaction.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemTransactionBinding
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.diff.TransactionDiffUtil
import timber.log.Timber

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var transactions = emptyList<Transaction>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val viewBinding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = TransactionViewHolder(viewBinding)
        return viewHolder
    }

    override fun getItemCount() = transactions.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    fun updateItems(newTransactions: List<Transaction>) {
        Timber.tag(App.TAG).d("newTransactions size : ${newTransactions.size}")
        val diffUtilsCallback =
            TransactionDiffUtil(
                transactions,
                newTransactions
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        diffUtilsResult.dispatchUpdatesTo(this)
        transactions = newTransactions
    }

    class TransactionViewHolder(private val viewBinding: ItemTransactionBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(transaction: Transaction) {
            viewBinding.run {
                itemTransactionCategory.text = transaction.category.name
                itemTransactionDescription.text = transaction.description
                val drawable = createTintDrawable(
                    context,
                    transaction.category.icon,
                    transaction.category.color
                )
                GlideApp
                    .with(context)
                    .load(drawable)
                    .apply(
                        RequestOptions().override(
                            IMAGE_SIZE_IN_PX,
                            IMAGE_SIZE_IN_PX
                        )
                    )
                    .into(itemTransactionIcon)
            }
        }

        companion object {
            private const val IMAGE_SIZE_IN_PX = 136
        }
    }
}

package com.d9tilov.moneymanager.transaction.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemTransactionBinding
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.diff.TransactionDiffUtil

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    var itemSwipeListener: OnItemSwipeListener<Transaction>? = null

    private var transactions = emptyList<Transaction>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val viewBinding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(viewBinding)
    }

    override fun getItemCount() = transactions.size
    override fun getItemId(position: Int) = transactions[position].id

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    fun updateItems(newTransactions: List<Transaction>) {
        val diffUtilsCallback =
            TransactionDiffUtil(
                transactions,
                newTransactions
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        diffUtilsResult.dispatchUpdatesTo(this)
        transactions = newTransactions
    }

    fun deleteItem(position: Int) {
        val transactionToDelete = transactions[position]
        itemSwipeListener?.onItemSwiped(transactionToDelete, position)
    }

    class TransactionViewHolder(private val viewBinding: ItemTransactionBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(transaction: Transaction) {
            viewBinding.run {
                itemTransactionCategory.text = transaction.category.name
                itemTransactionDescription.text = transaction.description
                itemTransactionSum.setValue(transaction.sum)
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

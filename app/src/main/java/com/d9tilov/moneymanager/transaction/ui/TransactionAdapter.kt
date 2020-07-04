package com.d9tilov.moneymanager.transaction.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.base.ui.recyclerview.adapter.StickyAdapter
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemTransactionBinding
import com.d9tilov.moneymanager.databinding.ItemTransactionHeaderBinding
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.HEADER
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.ITEM
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader

class TransactionAdapter :
    StickyAdapter<BaseTransaction, RecyclerView.ViewHolder, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    var itemSwipeListener: OnItemSwipeListener<Transaction>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            val viewBinding =
                ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TransactionViewHolder(viewBinding)
        } else {
            val viewBinding = ItemTransactionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            TransactionHeaderViewHolder(viewBinding)
        }
    }

    override fun getItemId(position: Int) = getItem(position).hashCode().toLong()

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is Transaction) {
            return ITEM
        }
        return HEADER
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItem(position)?.itemType ?: 0
        if (type == HEADER) {
            (holder as TransactionHeaderViewHolder).bind(getItem(position) as TransactionHeader)
        } else {
            (holder as TransactionViewHolder).bind(getItem(position) as Transaction)
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return (getItem(itemPosition) as TransactionHeader).position
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, headerPosition: Int) {
        val date = getItem(headerPosition) as TransactionHeader
        (holder as TransactionHeaderViewHolder).bind(date)
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return createViewHolder(parent, HEADER)
    }

    fun deleteItem(position: Int) {
        val transactionToDelete = getItem(position) as Transaction
        itemSwipeListener?.onItemSwiped(transactionToDelete, position)
    }

    class TransactionHeaderViewHolder(private val viewBinding: ItemTransactionHeaderBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(transactionHeader: TransactionHeader) {
            viewBinding.run { itemTransactionHeaderDate.text = transactionHeader.date.toString() }
        }
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

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<BaseTransaction>() {
            override fun areItemsTheSame(
                oldItem: BaseTransaction,
                newItem: BaseTransaction
            ): Boolean {
                return if (oldItem is Transaction && newItem is Transaction) {
                    oldItem.id == newItem.id
                } else if (oldItem is TransactionHeader && newItem is TransactionHeader) {
                    oldItem.date == newItem.date
                } else {
                    false
                }
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: BaseTransaction,
                newItem: BaseTransaction
            ): Boolean {
                return if (oldItem is Transaction && newItem is Transaction) {
                    oldItem == newItem
                } else if (oldItem is TransactionHeader && newItem is TransactionHeader) {
                    oldItem == newItem
                } else {
                    false
                }
            }
        }
    }
}

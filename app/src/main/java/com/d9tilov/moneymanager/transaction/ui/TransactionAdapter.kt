package com.d9tilov.moneymanager.transaction.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyAdapter
import com.d9tilov.moneymanager.core.util.TRANSACTION_DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.formatDate
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hide
import com.d9tilov.moneymanager.core.util.show
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
    var itemClickListener: OnItemClickListener<Transaction>? = null
    private var removedItemPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        if (viewType == ITEM) {
            val viewBinding =
                ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            viewHolder = TransactionViewHolder(viewBinding)
            viewBinding.root.setOnClickListener {
                val adapterPosition = viewHolder.bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(adapterPosition) as Transaction
                    itemClickListener?.onItemClick(item, adapterPosition)
                }
            }
        } else {
            val viewBinding = ItemTransactionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            viewHolder = TransactionHeaderViewHolder(viewBinding)
        }
        return viewHolder
    }

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
        return getItem(itemPosition)?.headerPosition ?: 0
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, headerPosition: Int) {
        (holder as TransactionHeaderViewHolder).bind(getItem(headerPosition) as TransactionHeader)
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return createViewHolder(parent, HEADER)
    }

    fun deleteItem(position: Int) {
        if (getItem(position) is Transaction) {
            removedItemPosition = position
            val transactionToDelete = getItem(position) as Transaction
            itemSwipeListener?.onItemSwiped(transactionToDelete, position)
        }
    }

    fun cancelDeletion() {
        notifyItemChanged(removedItemPosition)
    }

    class TransactionHeaderViewHolder(private val viewBinding: ItemTransactionHeaderBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(transactionHeader: TransactionHeader) {
            viewBinding.itemTransactionHeaderDate.text =
                formatDate(transactionHeader.date, TRANSACTION_DATE_FORMAT)
        }
    }

    class TransactionViewHolder(private val viewBinding: ItemTransactionBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(transaction: Transaction) {
            viewBinding.run {
                itemTransactionCategory.text = transaction.category.name
                itemTransactionCategory.setTextColor(
                    ContextCompat.getColor(
                        context,
                        transaction.category.color
                    )
                )
                if (transaction.isRegular) itemTransactionRegularIcon.show() else itemTransactionRegularIcon.hide()
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
                    DataConstants.DEFAULT_CURRENCY_CODE
                )
                if (transaction.currencyCode != DataConstants.DEFAULT_CURRENCY_CODE) {
                    itemTransactionUsdSum.show()
                } else {
                    itemTransactionUsdSum.gone()
                }
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

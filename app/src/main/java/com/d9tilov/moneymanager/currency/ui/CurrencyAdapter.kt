package com.d9tilov.moneymanager.currency.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.databinding.ItemCurrencyBinding
import timber.log.Timber

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private val currencies: MutableList<Currency> = mutableListOf()
    var itemClickListener: OnItemClickListener<Currency>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyViewHolder {
        val viewBinding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = CurrencyViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(currencies[adapterPosition], adapterPosition)
                check(adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount() = currencies.size

    fun updateItems(newCategories: List<Currency>) {
        Timber.tag(App.TAG).d("newCurrencies size : ${newCategories.size}")
        val diffUtilsCallback =
            CurrencyDiffUtil(
                currencies,
                newCategories
            )
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        currencies.clear()
        currencies.addAll(newCategories)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    private fun check(position: Int) {
        currencies.forEachIndexed { index, video ->
            video.takeIf { it.isBase }?.let {
                currencies[index] = it.copy(isBase = false)
                notifyItemChanged(index)
            }
            if (index == position) {
                currencies[index] = currencies[index].copy(isBase = true)
                notifyItemChanged(index)
            }
        }
    }

    class CurrencyViewHolder(private val viewBinding: ItemCurrencyBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(currency: Currency) {
            viewBinding.run {
                itemCurrencyIcon.text = CurrencyUtils.getCurrencyIcon(currency.code)
                itemCurrencyTitle.text = CurrencyUtils.getCurrencyFullName(currency.code)
                itemCurrencySubtitle.text = currency.code
                if (currency.isBase) {
                    itemCurrencyCheck.show()
                } else {
                    itemCurrencyCheck.gone()
                }
            }
        }
    }

    override fun getItemId(position: Int) = currencies[position].code.hashCode().toLong()
}

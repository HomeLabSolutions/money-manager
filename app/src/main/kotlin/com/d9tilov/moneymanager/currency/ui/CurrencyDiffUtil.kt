package com.d9tilov.moneymanager.currency.ui

import androidx.recyclerview.widget.DiffUtil
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency

class CurrencyDiffUtil(
    private val oldCurrenciesList: List<DomainCurrency>,
    private val newCurrenciesList: List<DomainCurrency>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCurrenciesList[oldItemPosition].id == newCurrenciesList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldCurrenciesList.size
    }

    override fun getNewListSize(): Int {
        return newCurrenciesList.size
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        return newCurrenciesList[newItemPosition].value
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCurrenciesList[oldItemPosition] == newCurrenciesList[newItemPosition]
    }
}

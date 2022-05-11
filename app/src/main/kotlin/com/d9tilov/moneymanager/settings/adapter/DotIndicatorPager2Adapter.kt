package com.d9tilov.moneymanager.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.d9tilov.moneymanager.R

class DotIndicatorPager2Adapter : RecyclerView.Adapter<ViewHolder>() {

    data class Card(val id: Int)

    val items = mutableListOf<Card>().apply {
        repeat(10) { add(Card(it)) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return object : ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_billing_intro_card, parent, false)
        ) {}
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Empty
    }
}

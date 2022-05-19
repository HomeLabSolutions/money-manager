package com.d9tilov.moneymanager.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.category.domain.entity.CategoryGroupItem
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.databinding.ItemCategoryGroupItemBinding

class CategoryGroupIconSetAdapter(
    private val groups: List<CategoryGroupItem>,
    private val clickListener: OnItemClickListener<CategoryGroupItem>
) :
    RecyclerView.Adapter<CategoryGroupIconSetAdapter.CategoryGroupItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryGroupItemViewHolder {
        val viewBinding =
            ItemCategoryGroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = CategoryGroupItemViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(groups[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: CategoryGroupItemViewHolder,
        position: Int
    ) = holder.bind(groups[position])

    override fun getItemCount(): Int = groups.size

    class CategoryGroupItemViewHolder(private val viewBinding: ItemCategoryGroupItemBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(groupItem: CategoryGroupItem) {
            viewBinding.itemCategoryGroupName.text = context.getString(groupItem.name)
        }
    }
}

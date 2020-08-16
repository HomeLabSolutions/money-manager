package com.d9tilov.moneymanager.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryIconBinding
import com.d9tilov.moneymanager.transaction.TransactionType

class CategoryIconSetAdapter(private val transactionType: TransactionType) :
    RecyclerView.Adapter<CategoryIconSetAdapter.CategoryIconViewHolder>() {

    var itemClickListener: OnItemClickListener<Int>? = null

    private val expenseCategoryIconList = listOf(
        R.drawable.ic_category_expense_cafe to R.color.category_yellow,
        R.drawable.ic_category_expense_car to R.color.category_light_green,
        R.drawable.ic_category_expense_car_service to R.color.category_violet,
        R.drawable.ic_category_expense_doctor to R.color.category_light_blue,
        R.drawable.ic_category_expense_relax to R.color.category_brick_red,
        R.drawable.ic_category_expense_food to R.color.category_orange,
        R.drawable.ic_category_internet to R.color.category_mint,
        R.drawable.ic_category_expense_home to R.color.category_mud_green,
        R.drawable.ic_category_expense_travels to R.color.category_flower_violet,
        R.drawable.ic_category_expense_grocery to R.color.category_light_violet,
        R.drawable.ic_category_expense_fuel to R.color.category_sad_blue
    )

    private val incomeCategoryIconList = listOf(
        R.drawable.ic_category_income_business to R.color.category_yellow,
        R.drawable.ic_category_income_part_time_job to R.color.category_light_green,
        R.drawable.ic_category_income_salary to R.color.category_violet,
        R.drawable.ic_category_income_sale to R.color.category_light_blue
    )

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryIconViewHolder {
        val viewBinding = ItemCategoryIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = CategoryIconViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(
                    if (transactionType == TransactionType.EXPENSE)
                        expenseCategoryIconList[adapterPosition].first else
                        incomeCategoryIconList[adapterPosition].first,
                    adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun getItemCount() =
        if (transactionType == TransactionType.EXPENSE) expenseCategoryIconList.size else incomeCategoryIconList.size

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        holder.bind(if (transactionType == TransactionType.EXPENSE) expenseCategoryIconList[position] else incomeCategoryIconList[position])
    }

    class CategoryIconViewHolder(private val viewBinding: ItemCategoryIconBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(categoryItemIcon: Pair<Int, Int>) {
            val tintDrawable =
                createTintDrawable(context, categoryItemIcon.first, categoryItemIcon.second)
            GlideApp
                .with(context)
                .load(tintDrawable)
                .into(viewBinding.categoryIcon)
        }
    }
}

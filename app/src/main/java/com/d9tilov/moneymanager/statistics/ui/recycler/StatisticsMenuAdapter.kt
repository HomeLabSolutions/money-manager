package com.d9tilov.moneymanager.statistics.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.databinding.ItemStatisticsMenuBinding
import com.d9tilov.moneymanager.databinding.ItemStatisticsMenuCurrencyBinding
import com.d9tilov.moneymanager.statistics.domain.BaseStatisticsMenuType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCategoryType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuChartMode
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCurrency
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuInStatistics
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuTransactionType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuType
import com.d9tilov.moneymanager.statistics.ui.recycler.diff.StatisticsMenuItemDiffUtils

class StatisticsMenuAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var chartMenuItemClickListener: OnItemClickListener<StatisticsMenuChartMode>? = null
    var currencyMenuItemClickListener: OnItemClickListener<StatisticsMenuCurrency>? = null
    var inStatisticsMenuItemClickListener: OnItemClickListener<StatisticsMenuInStatistics>? = null
    var trTypeMenuItemClickListener: OnItemClickListener<StatisticsMenuTransactionType>? = null
    var categoryMenuItemClickListener: OnItemClickListener<StatisticsMenuCategoryType>? = null
    private var menuItems = mutableListOf<BaseStatisticsMenuType>()

    init {
        setHasStableIds(true)
    }

    fun updateItems(newMenuItems: List<BaseStatisticsMenuType>) {
        val diffUtilsCallback = StatisticsMenuItemDiffUtils(menuItems, newMenuItems)
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        menuItems.clear()
        menuItems.addAll(newMenuItems)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return when (viewType) {
            0 -> {
                val viewBinding = ItemStatisticsMenuCurrencyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = CurrencyModeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        currencyMenuItemClickListener?.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuCurrency,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            1 -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = ChartModeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        chartMenuItemClickListener?.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuChartMode,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            2 -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = CategoryTypeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        categoryMenuItemClickListener?.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuCategoryType,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            3 -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = TransactionTypeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        trTypeMenuItemClickListener?.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuTransactionType,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            4 -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = InStatisticsViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        inStatisticsMenuItemClickListener?.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuInStatistics,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            else -> throw IllegalArgumentException("Wrong ViewType: $viewType")
        }
    }

    override fun getItemCount(): Int = menuItems.size

    override fun getItemId(position: Int) = menuItems[position].menuType.id.toLong()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as CurrencyModeViewHolder).bind(menuItems[position] as StatisticsMenuCurrency)
            1 -> (holder as ChartModeViewHolder).bind(menuItems[position] as StatisticsMenuChartMode)
            2 -> (holder as CategoryTypeViewHolder).bind(menuItems[position] as StatisticsMenuCategoryType)
            3 -> (holder as TransactionTypeViewHolder).bind(menuItems[position] as StatisticsMenuTransactionType)
            4 -> (holder as InStatisticsViewHolder).bind(menuItems[position] as StatisticsMenuInStatistics)
        }
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> StatisticsMenuType.CURRENCY.id
        1 -> StatisticsMenuType.CHART.id
        2 -> StatisticsMenuType.CATEGORY_TYPE.id
        3 -> StatisticsMenuType.TRANSACTION_TYPE.id
        4 -> StatisticsMenuType.STATISTICS.id
        else -> throw IllegalArgumentException("Unknown ViewType for position: $position")
    }

    class ChartModeViewHolder(private val viewBinding: ItemStatisticsMenuBinding) :
        BaseViewHolder(viewBinding) {
        fun bind(mode: StatisticsMenuChartMode) {
            viewBinding.statisticsMenuTitle.text =
                context.getString(R.string.statistics_menu_chart_type_title)
            when (mode) {
                StatisticsMenuChartMode.LINE_CHART ->
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_line_chart)
                StatisticsMenuChartMode.PIE_CHART ->
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_pie_chart)
            }
        }
    }

    class CurrencyModeViewHolder(private val viewBinding: ItemStatisticsMenuCurrencyBinding) :
        BaseViewHolder(viewBinding) {
        fun bind(currencyType: StatisticsMenuCurrency) {
            viewBinding.statisticsMenuCurrencyIcon.text =
                CurrencyUtils.getCurrencyIcon(currencyType.currencyCode)
        }
    }

    class InStatisticsViewHolder(private val viewBinding: ItemStatisticsMenuBinding) :
        BaseViewHolder(viewBinding) {
        fun bind(inStatisticsType: StatisticsMenuInStatistics) {
            when (inStatisticsType) {
                StatisticsMenuInStatistics.IN_STATISTICS -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_in_statistics)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_in_statistics_title)
                }
                StatisticsMenuInStatistics.ALL -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_not_in_statistics)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_not_in_statistics_title)
                }
            }
        }
    }

    class TransactionTypeViewHolder(private val viewBinding: ItemStatisticsMenuBinding) :
        BaseViewHolder(viewBinding) {
        fun bind(type: StatisticsMenuTransactionType) {
            when (type) {
                StatisticsMenuTransactionType.INCOME -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_statistics_income)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_incomes_title)
                }
                StatisticsMenuTransactionType.EXPENSE -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_statistics_expense)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_expenses_title)
                }
            }
        }
    }

    class CategoryTypeViewHolder(private val viewBinding: ItemStatisticsMenuBinding) :
        BaseViewHolder(viewBinding) {
        fun bind(type: StatisticsMenuCategoryType) {
            when (type) {
                StatisticsMenuCategoryType.PARENT -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_statistics_root)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_category_type_parent_title)
                }
                StatisticsMenuCategoryType.CHILD -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_statistics_child)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_category_type_child_title)
                }
            }
        }
    }
}

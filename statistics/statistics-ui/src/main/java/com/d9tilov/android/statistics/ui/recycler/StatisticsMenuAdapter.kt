package com.d9tilov.android.statistics.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.android.common_android.ui.base.BaseViewHolder
import com.d9tilov.android.core.events.OnItemClickListener
import com.d9tilov.android.core.utils.CurrencyUtils
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuCategoryType
import com.d9tilov.android.statistics.data.model.StatisticsMenuChartMode
import com.d9tilov.android.statistics.data.model.StatisticsMenuCurrency
import com.d9tilov.android.statistics.data.model.StatisticsMenuInStatistics
import com.d9tilov.android.statistics.data.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.data.model.toType
import com.d9tilov.android.statistics.ui.recycler.diff.StatisticsMenuItemDiffUtils
import com.example.statistics_ui.R
import com.example.statistics_ui.databinding.ItemStatisticsMenuBinding
import com.example.statistics_ui.databinding.ItemStatisticsMenuCurrencyBinding

class StatisticsMenuAdapter(
    private val chartMenuItemClickListener: OnItemClickListener<StatisticsMenuChartMode>,
    private val currencyMenuItemClickListener: OnItemClickListener<StatisticsMenuCurrency>,
    private val inStatisticsMenuItemClickListener: OnItemClickListener<StatisticsMenuInStatistics>,
    private val trTypeMenuItemClickListener: OnItemClickListener<StatisticsMenuTransactionType>,
    private val categoryMenuItemClickListener: OnItemClickListener<StatisticsMenuCategoryType>
) : RecyclerView.Adapter<BaseViewHolder>() {

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
            StatisticsMenuType.CURRENCY.ordinal -> {
                val viewBinding = ItemStatisticsMenuCurrencyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = CurrencyModeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        currencyMenuItemClickListener.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuCurrency,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            StatisticsMenuType.CHART.ordinal -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = ChartModeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        chartMenuItemClickListener.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuChartMode,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            StatisticsMenuType.CATEGORY_TYPE.ordinal -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = CategoryTypeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        categoryMenuItemClickListener.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuCategoryType,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            StatisticsMenuType.TRANSACTION_TYPE.ordinal -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = TransactionTypeViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        trTypeMenuItemClickListener.onItemClick(
                            menuItems[adapterPosition] as StatisticsMenuTransactionType,
                            adapterPosition
                        )
                    }
                }
                viewHolder
            }
            StatisticsMenuType.STATISTICS.ordinal -> {
                val viewBinding = ItemStatisticsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = InStatisticsViewHolder(viewBinding)
                viewBinding.root.setOnClickListener {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        inStatisticsMenuItemClickListener.onItemClick(
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

    override fun getItemId(position: Int) = menuItems[position].menuType.ordinal.toLong()
    override fun getItemCount(): Int = menuItems.size
    override fun getItemViewType(position: Int): Int = StatisticsMenuType.values()[position].ordinal

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (toType(holder.itemViewType)) {
            StatisticsMenuType.CURRENCY -> (holder as CurrencyModeViewHolder).bind(menuItems[position] as StatisticsMenuCurrency)
            StatisticsMenuType.CHART -> (holder as ChartModeViewHolder).bind(menuItems[position] as StatisticsMenuChartMode)
            StatisticsMenuType.CATEGORY_TYPE -> (holder as CategoryTypeViewHolder).bind(menuItems[position] as StatisticsMenuCategoryType)
            StatisticsMenuType.TRANSACTION_TYPE -> (holder as TransactionTypeViewHolder).bind(menuItems[position] as StatisticsMenuTransactionType)
            StatisticsMenuType.STATISTICS -> (holder as InStatisticsViewHolder).bind(menuItems[position] as StatisticsMenuInStatistics)
        }
    }

    class ChartModeViewHolder(private val viewBinding: ItemStatisticsMenuBinding) :
        BaseViewHolder(viewBinding) {
        fun bind(mode: StatisticsMenuChartMode) {
            viewBinding.statisticsMenuTitle.text = context.getString(R.string.statistics_menu_chart_type_title)
            when (mode) {
                StatisticsMenuChartMode.LineChart -> viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_line_chart)
                StatisticsMenuChartMode.PieChart -> viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_pie_chart)
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
                StatisticsMenuInStatistics.InStatistics -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_in_statistics)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_in_statistics_title)
                }
                StatisticsMenuInStatistics.All -> {
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
                StatisticsMenuTransactionType.Income -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_statistics_income)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_incomes_title)
                }
                StatisticsMenuTransactionType.Expense -> {
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
                StatisticsMenuCategoryType.Parent -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_statistics_root)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_category_type_parent_title)
                }
                StatisticsMenuCategoryType.Child -> {
                    viewBinding.statisticsMenuIcon.setImageResource(R.drawable.ic_statistics_child)
                    viewBinding.statisticsMenuTitle.text =
                        context.getString(R.string.statistics_menu_category_type_child_title)
                }
            }
        }
    }
}

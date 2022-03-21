package com.d9tilov.moneymanager.statistics.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.util.DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.isSameDay
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.toLocalDateTime
import com.d9tilov.moneymanager.core.util.toMillis
import com.d9tilov.moneymanager.databinding.FragmentStatisticsBinding
import com.d9tilov.moneymanager.databinding.LayoutEmptyStatisticsPlaceholderBinding
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCategoryType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuChartMode
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuChartMode.LINE_CHART
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuChartMode.PIE_CHART
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCurrency
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuInStatistics
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuTransactionType
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod.CUSTOM
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod.DAY
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod.MONTH
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod.WEEK
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod.YEAR
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class StatisticsFragment :
    BaseFragment<StatisticsNavigator, FragmentStatisticsBinding>(
        FragmentStatisticsBinding::inflate,
        R.layout.fragment_statistics
    ),
    StatisticsNavigator,
    OnChartValueSelectedListener {

    override fun getNavigator() = this
    override val viewModel by viewModels<StatisticsViewModel>()
    private val statisticsBarChartAdapter: StatisticsBarChartAdapter = StatisticsBarChartAdapter()
    private val statisticsMenuAdapter: StatisticsMenuAdapter = StatisticsMenuAdapter()
    private var emptyViewStub: LayoutEmptyStatisticsPlaceholderBinding? = null
    private var showMenu = false
    private val hideMenuHandler = Handler(Looper.getMainLooper())

    private val onCharMenuItemClickListener =
        object : OnItemClickListener<StatisticsMenuChartMode> {
            override fun onItemClick(item: StatisticsMenuChartMode, position: Int) {
                viewModel.updateCharMode()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            }
        }
    private val onCurrencyMenuItemClickListener =
        object : OnItemClickListener<StatisticsMenuCurrency> {
            override fun onItemClick(item: StatisticsMenuCurrency, position: Int) {
                viewModel.updateCurrency()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            }
        }
    private val onInStatisticsMenuItemClickListener =
        object : OnItemClickListener<StatisticsMenuInStatistics> {
            override fun onItemClick(item: StatisticsMenuInStatistics, position: Int) {
                viewModel.updateStatisticsFlag()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            }
        }

    private val onTrTypeMenuItemClickListener =
        object : OnItemClickListener<StatisticsMenuTransactionType> {
            override fun onItemClick(item: StatisticsMenuTransactionType, position: Int) {
                viewModel.updateTransactionType()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            }
        }
    private val onCategoryItemClickListener =
        object : OnItemClickListener<StatisticsMenuCategoryType> {
            override fun onItemClick(item: StatisticsMenuCategoryType, position: Int) {
                viewModel.updateCategoryType()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        statisticsMenuAdapter.chartMenuItemClickListener = onCharMenuItemClickListener
        statisticsMenuAdapter.currencyMenuItemClickListener = onCurrencyMenuItemClickListener
        statisticsMenuAdapter.inStatisticsMenuItemClickListener =
            onInStatisticsMenuItemClickListener
        statisticsMenuAdapter.trTypeMenuItemClickListener = onTrTypeMenuItemClickListener
        statisticsMenuAdapter.categoryMenuItemClickListener = onCategoryItemClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenu = false
        setChartMode(viewModel.chartMode, viewModel.chartPeriod)
        viewBinding?.run {
            statisticsBarChart.adapter = statisticsBarChartAdapter
            emptyViewStub = statisticsEmptyPlaceholder
            initStatisticsMenuRv()
            statisticsDay.setOnClickListener { setChartMode(PIE_CHART, DAY) }
            statisticsWeek.setOnClickListener { setChartMode(PIE_CHART, WEEK) }
            statisticsMonth.setOnClickListener { setChartMode(PIE_CHART, MONTH) }
            statisticsYear.setOnClickListener { setChartMode(PIE_CHART, YEAR) }
            statisticsCustom.setOnClickListener { openRangeCalendar() }
            statisticsMenu.setOnClickListener { if (!showMenu) showMenu() else hideMenu() }
        }

        initPieChart()
        viewModel.getTransactions().observe(viewLifecycleOwner) { list ->
            val newList = list.map { item ->
                if (item.category.children.isNotEmpty()) {
                    item.copy(category = item.category.copy(color = item.category.children[0].color))
                } else item
            }
            setPieChartData(newList)
            statisticsBarChartAdapter.updateItems(newList)
            viewBinding?.statisticsSpentInPeriodSum?.setValue(
                newList.sumOf { it.sum },
                viewModel.currencyType.currencyCode
            )
            viewBinding?.statisticsBarChart?.scrollToPosition(0)
        }
        viewModel.currency.observe(viewLifecycleOwner) { code ->
            viewModel.updateCurrency(code)
            statisticsMenuAdapter.updateItems(viewModel.menuItemList)
        }
    }

    override fun onStop() {
        super.onStop()
        hideMenuHandler.removeCallbacksAndMessages(null)
        hideMenu()
    }

    private fun initStatisticsMenuRv() {
        val layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        viewBinding?.run {
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(statisticsMenuRv)
            statisticsMenuRv.layoutManager = layoutManager
            statisticsMenuRv.adapter = statisticsMenuAdapter
        }
    }

    private fun showMenu() {
        showMenu = true
        viewBinding?.run {
            statisticsMenu.setImageResource(R.drawable.ic_statistics_close)
            statisticsMenuRv.show()
        }
    }

    private fun hideMenu() {
        showMenu = false
        viewBinding?.run {
            statisticsMenu.setImageResource(R.drawable.ic_statistics_menu)
            statisticsMenuRv.gone()
        }
    }

    private fun hideMenuWithDelay() {
        hideMenuHandler.removeCallbacksAndMessages(null)
        hideMenuHandler.postDelayed({ hideMenu() }, HIDE_MENU_DELAY)
    }

    private fun openRangeCalendar() {
        val builderRange = MaterialDatePicker.Builder.dateRangePicker()
        val timezone = TimeZone.getDefault()
        val startDateSelection = viewModel.from.toMillis()
        val endDateSelection = viewModel.to.toMillis()
        val startSelectionMillis = startDateSelection + timezone.getOffset(startDateSelection)
        val endSelectionMillis = endDateSelection + timezone.getOffset(endDateSelection)
        builderRange
            .setCalendarConstraints(limitRange().build())
            .setSelection(Pair(startSelectionMillis, endSelectionMillis))
        val pickerRange = builderRange.build()
        pickerRange.addOnPositiveButtonClickListener { startEndPair: Pair<Long, Long> ->
            val startDate = startEndPair.first.toLocalDateTime().toMillis()
            val endDate = startEndPair.second.toLocalDateTime().toMillis()
            val startMillis = startDate - timezone.getOffset(startDate)
            val endMillis = endDate - timezone.getOffset(endDate)
            setChartMode(
                PIE_CHART,
                CUSTOM,
                Pair(startMillis.toLocalDateTime(), endMillis.toLocalDateTime())
            )
        }
        pickerRange.show(parentFragmentManager, pickerRange.toString())
    }

    private fun limitRange(): CalendarConstraints.Builder {
        val constraintsBuilderRange = CalendarConstraints.Builder()
        constraintsBuilderRange.setEnd(currentDateTime().toMillis())
        constraintsBuilderRange.setValidator(DateValidatorPointBackward.now())
        return constraintsBuilderRange
    }

    private fun updatePeriod() {
        val to = currentDate()
        var toLocalDateTime = to.getEndOfDay()
        val fromLocalDateTime = when (viewModel.chartPeriod) {
            DAY -> to.getEndOfDay()
            WEEK -> to.minus(1, DateTimeUnit.WEEK).getEndOfDay()
            MONTH -> to.minus(1, DateTimeUnit.MONTH).getEndOfDay()
            YEAR -> to.minus(1, DateTimeUnit.YEAR).getEndOfDay()
            CUSTOM -> {
                toLocalDateTime = viewModel.to
                viewModel.from
            }
        }

        viewBinding?.statisticsPieChart?.centerText =
            if (toLocalDateTime.isSameDay(fromLocalDateTime)) getString(R.string.statistics_today)
            else {
                val fromMillis = Date(fromLocalDateTime.toMillis())
                val toMillis = Date(toLocalDateTime.getEndOfDay().toMillis())
                val sb = StringBuilder()
                sb.append(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(fromMillis))
                sb.append(" - ")
                sb.append(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(toMillis))
                sb.toString()
            }
        viewBinding?.statisticsPieChart?.setCenterTextColor(
            ContextCompat.getColor(requireContext(), R.color.control_activated_color)
        )
    }

    private fun initPieChart() {
        val tfRegular = Typeface.createFromAsset(requireContext().assets, "OpenSans-Regular.ttf")
        viewBinding?.run {
            statisticsPieChart.setUsePercentValues(true)
            statisticsPieChart.description.isEnabled = false
            statisticsPieChart.setExtraOffsets(10f, 0f, 10f, 0f)

            statisticsPieChart.dragDecelerationFrictionCoef = 0.95f

            statisticsPieChart.isDrawHoleEnabled = true
            statisticsPieChart.setHoleColor(Color.TRANSPARENT)

            statisticsPieChart.holeRadius = 48f
            statisticsPieChart.transparentCircleRadius = 48f

            statisticsPieChart.setDrawCenterText(true)

            statisticsPieChart.rotationAngle = 0f
            statisticsPieChart.isRotationEnabled = true
            statisticsPieChart.isHighlightPerTapEnabled = false

            statisticsPieChart.setOnChartValueSelectedListener(this@StatisticsFragment)
            statisticsPieChart.animateY(ANIMATION_DURATION, Easing.EaseInOutQuad)
            statisticsPieChart.setEntryLabelColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary_color
                )
            )
            statisticsPieChart.setEntryLabelTypeface(tfRegular)
            statisticsPieChart.setEntryLabelTextSize(15f)
            statisticsPieChart.setCenterTextSize(16f)
            statisticsPieChart.legend.isEnabled = false
        }
    }

    private fun setPieChartData(list: List<TransactionChartModel>) {
        if (list.isEmpty()) {
            showViewStub()
            return
        }
        hideViewStub()
        val dataSet = PieDataSet(
            list
                .map { tr ->
                    val categoryName = tr.category.name
                    val displayName = if (tr.percent > PERCENT_LIMIT_TO_SHOW_LABEL) {
                        if (categoryName.length > MAX_CATEGORY_NAME_LENGTH)
                            "${categoryName.dropLast(categoryName.length - MAX_CATEGORY_NAME_LENGTH)}..."
                        else categoryName
                    } else ""
                    PieEntry(
                        tr.sum.toFloat(),
                        displayName,
                        createTintDrawable(requireContext(), tr.category.icon, tr.category.color)
                    )
                },
            ""
        )
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f
        dataSet.colors = list.map { ContextCompat.getColor(requireContext(), it.category.color) }
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(viewBinding?.statisticsPieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
        viewBinding?.statisticsPieChart?.data = data

        updatePeriod()
        viewBinding?.statisticsPieChart?.highlightValues(null)
        viewBinding?.statisticsPieChart?.invalidate()
    }

    private fun setChartMode(
        mode: StatisticsMenuChartMode,
        period: StatisticsPeriod = MONTH,
        periodPair: Pair<LocalDateTime, LocalDateTime>? = null
    ) {
        val oldView = getViewFromPeriod(viewModel.chartPeriod)
        oldView?.isSelected = false
        val newView = getViewFromPeriod(period)
        newView?.isSelected = true
        if (period != CUSTOM) {
            viewModel.updatePeriod(period)
        } else {
            viewModel.updatePeriod(period, periodPair)
        }
        when (mode) {
            PIE_CHART -> {}
            LINE_CHART -> {}
        }
    }

    private fun getViewFromPeriod(period: StatisticsPeriod): TextView? {
        return viewBinding?.run {
            when (period) {
                DAY -> statisticsDay
                WEEK -> statisticsWeek
                MONTH -> statisticsMonth
                YEAR -> statisticsYear
                CUSTOM -> statisticsCustom
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {}

    override fun onNothingSelected() {}

    private fun showViewStub() {
        emptyViewStub?.root?.show()
    }

    private fun hideViewStub() {
        emptyViewStub?.root?.gone()
    }

    companion object {
        private val PERCENT_LIMIT_TO_SHOW_LABEL = BigDecimal(6)
        private const val MAX_CATEGORY_NAME_LENGTH = 10
        private const val ANIMATION_DURATION = 500
        private const val HIDE_MENU_DELAY = 5000L
    }
}

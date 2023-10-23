package com.d9tilov.android.statistics.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.common.android.ui.recyclerview.ItemSnapHelper
import com.d9tilov.android.common.android.utils.DATE_FORMAT
import com.d9tilov.android.common.android.utils.TRANSACTION_DATE_FORMAT_SHORT
import com.d9tilov.android.common.android.utils.createTintDrawable
import com.d9tilov.android.common.android.utils.getColorFromAttr
import com.d9tilov.android.common.android.utils.gone
import com.d9tilov.android.common.android.utils.hideWithAnimation
import com.d9tilov.android.common.android.utils.let2
import com.d9tilov.android.common.android.utils.show
import com.d9tilov.android.common.android.utils.showWithAnimation
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.isSameDay
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.statistics.data.model.StatisticsMenuChartMode
import com.d9tilov.android.statistics.data.model.StatisticsMenuInStatistics
import com.d9tilov.android.statistics.data.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.data.model.StatisticsPeriod
import com.d9tilov.android.statistics.ui.navigation.StatisticsNavigator
import com.d9tilov.android.statistics.ui.recycler.StatisticsBarChartAdapter
import com.d9tilov.android.statistics.ui.recycler.StatisticsMenuAdapter
import com.d9tilov.android.statistics.ui.vm.StatisticsViewModel
import com.d9tilov.android.statistics_ui.R
import com.d9tilov.android.statistics_ui.databinding.FragmentStatisticsBinding
import com.d9tilov.android.statistics_ui.databinding.LayoutEmptyStatisticsPlaceholderBinding
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionLineChartModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
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
    StatisticsNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<StatisticsViewModel>()
    private val statisticsBarChartAdapter: StatisticsBarChartAdapter =
        StatisticsBarChartAdapter { item, _ ->
            val action = StatisticsFragmentDirections.toStatisticsDetailsDest(
                item.category.id,
                viewModel.chartPeriod.from.toMillis(),
                viewModel.chartPeriod.to.toMillis(),
                viewModel.inStatistics == StatisticsMenuInStatistics.InStatistics,
                when (viewModel.transactionType) {
                    StatisticsMenuTransactionType.Expense -> TransactionType.EXPENSE.value
                    StatisticsMenuTransactionType.Income -> TransactionType.INCOME.value
                }
            )
            findNavController().navigate(action)
        }
    private val statisticsMenuAdapter: StatisticsMenuAdapter by lazy {
        StatisticsMenuAdapter(
            chartMenuItemClickListener = { _, _ ->
                viewModel.updateCharMode()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
                showChart()
            },
            currencyMenuItemClickListener = { _, _ ->
                viewModel.updateCurrency()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            },
            inStatisticsMenuItemClickListener = { _, _ ->
                viewModel.updateStatisticsFlag()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            },
            trTypeMenuItemClickListener = { _, _ ->
                viewModel.updateTransactionType()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            },
            categoryMenuItemClickListener = { _, _ ->
                viewModel.updateCategoryType()
                statisticsMenuAdapter.updateItems(viewModel.menuItemList)
                hideMenuWithDelay()
            }
        )
    }
    private var emptyViewStub: LayoutEmptyStatisticsPlaceholderBinding? = null
    private var showMenu = false
    private val hideMenuHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenu = false
        updatePeriod(viewModel.chartPeriod)
        showChart()
        viewBinding?.run {
            statisticsBarChart.adapter = statisticsBarChartAdapter
            statisticsBarChart.layoutManager = LinearLayoutManager(requireContext())
            emptyViewStub = statisticsEmptyPlaceholder
            initStatisticsMenuRv()
            statisticsDay.setOnClickListener { updatePeriod(StatisticsPeriod.DAY) }
            statisticsWeek.setOnClickListener { updatePeriod(StatisticsPeriod.WEEK) }
            statisticsMonth.setOnClickListener { updatePeriod(StatisticsPeriod.MONTH) }
            statisticsYear.setOnClickListener { updatePeriod(StatisticsPeriod.YEAR) }
            statisticsCustom.setOnClickListener { openRangeCalendar() }
            statisticsMenu.setOnClickListener { if (!showMenu) showMenu() else hideMenu() }
            initPieChart()
            initLineChart()
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.getTransactions()
                            .collect { result ->
                                when (result) {
                                    is ResultOf.Success -> {
                                        val list = result.data
                                        val newList = list.map { item ->
                                            if (item.category.children.isNotEmpty()) {
                                                item.copy(category = item.category.copy(color = item.category.children[0].color))
                                            } else {
                                                item
                                            }
                                        }
                                        statisticsBarChartAdapter.updateItems(newList)
                                        statisticsSpentInPeriodSum.setValue(
                                            newList.sumOf { it.sum },
                                            viewModel.currencyType.currencyCode
                                        )
                                        statisticsBarChart.scrollToPosition(0)
                                        setPieChartData(newList)
                                    }

                                    else -> {}
                                }
                            }
                    }
                    launch {
                        viewModel.getTransactionsInCurrentPeriod().collect { result ->
                            when (result) {
                                is ResultOf.Success -> setLineChartData(result.data)
                                else -> {}
                            }
                        }
                    }
                    launch {
                        viewModel.isPremium.collect { isPremium ->
                            if (isPremium) {
                                statisticsMenu.show()
                            } else {
                                statisticsMenu.gone()
                            }
                        }
                    }
                }
            }
        }
        statisticsMenuAdapter.updateItems(viewModel.menuItemList)
    }

    override fun onStop() {
        super.onStop()
        hideMenuHandler.removeCallbacksAndMessages(null)
        hideMenu()
    }

    private fun showChart() {
        viewBinding?.run {
            when (viewModel.chartMode) {
                StatisticsMenuChartMode.PieChart -> {
                    statisticsPieChart.showWithAnimation()
                    statisticsLineChart.hideWithAnimation()
                }

                StatisticsMenuChartMode.LineChart -> {
                    statisticsPieChart.hideWithAnimation()
                    statisticsLineChart.showWithAnimation()
                }
            }
        }
    }

    private fun hideChart() {
        viewBinding?.run {
            statisticsPieChart.gone()
            statisticsLineChart.gone()
        }
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
        val period = viewModel.chartPeriod
        val startDateSelection =
            if (period is StatisticsPeriod.CUSTOM) {
                period.from.toMillis()
            } else {
                currentDate().getStartOfDay().toMillis()
            }
        val endDateSelection =
            if (period is StatisticsPeriod.CUSTOM) {
                period.to.toMillis()
            } else {
                currentDate().getEndOfDay().toMillis()
            }
        val startSelectionMillis = startDateSelection + timezone.getOffset(startDateSelection)
        val endSelectionMillis = endDateSelection + timezone.getOffset(endDateSelection)
        builderRange
            .setCalendarConstraints(limitRange().build())
            .setSelection(androidx.core.util.Pair(startSelectionMillis, endSelectionMillis))
        val pickerRange = builderRange.build()
        pickerRange.addOnPositiveButtonClickListener { startEndPair: androidx.core.util.Pair<Long, Long> ->
            val startDate = startEndPair.first.toLocalDateTime().toMillis()
            val endDate = startEndPair.second.toLocalDateTime().toMillis()
            val startMillis = startDate - timezone.getOffset(startDate)
            val endMillis = endDate - timezone.getOffset(endDate)
            updatePeriod(
                StatisticsPeriod.CUSTOM(
                    startMillis.toLocalDateTime(),
                    endMillis.toLocalDateTime()
                )
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
        val toLocalDateTime = viewModel.chartPeriod.to
        val fromLocalDateTime = viewModel.chartPeriod.from
        viewBinding?.statisticsPieChart?.centerText =
            if (toLocalDateTime.isSameDay(fromLocalDateTime)) {
                getString(R.string.statistics_today)
            } else {
                val fromMillis = Date(fromLocalDateTime.toMillis())
                val toMillis = Date(toLocalDateTime.getEndOfDay().toMillis())
                val sb = StringBuilder()
                sb.append(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(fromMillis))
                sb.append(" - ")
                sb.append(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(toMillis))
                sb.toString()
            }
        val centerColor = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorOnBackground)
        viewBinding?.statisticsPieChart?.setCenterTextColor(centerColor)
        viewBinding?.statisticsPieChart?.invalidate()
    }

    private fun initPieChart() {
        val tfRegular = Typeface.createFromAsset(requireContext().assets, "OpenSans-Regular.ttf")
        viewBinding?.run {
            statisticsPieChart.setUsePercentValues(true)
            statisticsPieChart.description.isEnabled = false
            statisticsPieChart.setExtraOffsets(
                PIE_CHART_LEFT_OFFSET,
                PIE_CHART_TOP_OFFSET,
                PIE_CHART_RIGHT_OFFSET,
                PIE_CHART_BOTTOM_OFFSET
            )

            statisticsPieChart.dragDecelerationFrictionCoef =
                PIE_CHART_DRAG_DECELERATION_FRICTION_COEF

            statisticsPieChart.isDrawHoleEnabled = true
            statisticsPieChart.setHoleColor(Color.TRANSPARENT)

            statisticsPieChart.holeRadius = PIE_CHART_RADIUS
            statisticsPieChart.transparentCircleRadius = PIE_CHART_RADIUS

            statisticsPieChart.setDrawCenterText(true)

            statisticsPieChart.rotationAngle = PIE_CHART_ROTATION_ANGEL
            statisticsPieChart.isRotationEnabled = true
            statisticsPieChart.isHighlightPerTapEnabled = false

            statisticsPieChart.animateY(ANIMATION_DURATION, Easing.EaseInOutQuad)
            statisticsPieChart.setEntryLabelColor(requireContext().getColorFromAttr(com.google.android.material.R.attr.colorOnBackground))
            statisticsPieChart.setEntryLabelTypeface(tfRegular)
            statisticsPieChart.setEntryLabelTextSize(PIE_CHART_ENTRY_LABEL_TEXT_SIZE)
            statisticsPieChart.setCenterTextSize(PIE_CHART_CENTER_TEXT_SIZE)
            statisticsPieChart.legend.isEnabled = false
        }
    }

    private fun setPieChartData(data: List<TransactionChartModel>) {
        if (data.isEmpty()) {
            showViewStub()
            return
        }
        hideViewStub()
        val dataSet = PieDataSet(
            data
                .map { tr ->
                    val categoryName = tr.category.name
                    val displayName = if (tr.percent > PERCENT_LIMIT_TO_SHOW_LABEL) {
                        if (categoryName.length > MAX_CATEGORY_NAME_LENGTH) {
                            "${categoryName.dropLast(categoryName.length - MAX_CATEGORY_NAME_LENGTH)}..."
                        } else {
                            categoryName
                        }
                    } else {
                        ""
                    }
                    let2(tr.category.icon, tr.category.color) { icon, color ->
                        PieEntry(
                            tr.sum.toFloat(),
                            displayName,
                            createTintDrawable(requireContext(), icon, color)
                        )
                    }
                },
            ""
        )
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = PIE_CHART_SLICE_SPACE
        dataSet.iconsOffset = MPPointF(PIE_CHART_ICONS_X_OFFSET, PIE_CHART_ICONS_Y_OFFSET)
        dataSet.selectionShift = PIE_CHART_SELECTION_SHIFT
        dataSet.valueLinePart1OffsetPercentage = PIE_CHART_VALUE_LINE_PART_OFFSET_PERCENTAGE
        dataSet.valueLinePart1Length = PIE_CHART_VALUE_LINE_PART1_LENGTH
        dataSet.valueLinePart2Length = PIE_CHART_VALUE_LINE_PART2_LENGTH
        dataSet.colors = data
            .mapNotNull { model -> model.category.color }
            .map { color -> ContextCompat.getColor(requireContext(), color) }
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter(viewBinding?.statisticsPieChart))
        pieData.setValueTextSize(PIE_CHART_VALUE_TEXT_SIZE)
        pieData.setValueTextColor(requireContext().getColorFromAttr(com.google.android.material.R.attr.backgroundColor))
        viewBinding?.statisticsPieChart?.data = pieData

        viewBinding?.statisticsPieChart?.highlightValues(null)
        viewBinding?.statisticsPieChart?.invalidate()
    }

    private fun initLineChart() {
        viewBinding?.run {
            statisticsLineChart.setTouchEnabled(true)
            statisticsLineChart.isDragEnabled = true
            statisticsLineChart.setScaleEnabled(true)
            statisticsLineChart.setPinchZoom(true)
            statisticsLineChart.setMaxVisibleValueCount(LINE_CHART_MAX_VISIBLE_VALUE_COUNT)
            statisticsLineChart.description.isEnabled = false
            val xAxis: XAxis = statisticsLineChart.xAxis
            xAxis.textColor = requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
            xAxis.enableGridDashedLine(
                LINE_CHART_LINE_LENGTH,
                LINE_CHART_SPACE_LENGTH_AXIS,
                LINE_CHART_PHASE
            )
            val yAxis: YAxis = statisticsLineChart.axisLeft
            yAxis.textColor = requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
            yAxis.enableGridDashedLine(
                LINE_CHART_LINE_LENGTH,
                LINE_CHART_SPACE_LENGTH_AXIS,
                LINE_CHART_PHASE
            )

            statisticsLineChart.axisRight.isEnabled = false
            statisticsLineChart.legend.isEnabled = false
            statisticsLineChart.animateX(ANIMATION_DURATION)
        }
    }

    private fun setLineChartData(data: Map<LocalDateTime, TransactionLineChartModel>) {
        if (data.isEmpty()) {
            showViewStub()
            return
        }
        hideViewStub()
        val values = data.toList()
            .mapIndexed { index, pair -> Entry(index.toFloat(), pair.second.sum.toFloat()) }
        viewBinding?.run {
            val lineDataSet = LineDataSet(values, "")
            lineDataSet.circleRadius = LINE_CHART_DATA_CIRCLE_RADIUS
            lineDataSet.formSize = LINE_CHART_DATA_FORM_SIZE
            lineDataSet.valueTextSize = LINE_CHART_DATA_LABEL_TEXT_SIZE
            lineDataSet.valueTextColor = requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
            lineDataSet.enableDashedHighlightLine(
                LINE_CHART_LINE_LENGTH,
                LINE_CHART_SPACE_LENGTH,
                LINE_CHART_PHASE
            )
            lineDataSet.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.statistics_fade_line_chart)
            lineDataSet.setDrawFilled(true)
            lineDataSet.setFillFormatter { _, _ -> statisticsLineChart.axisLeft.axisMinimum }
            val dataSets = mutableListOf<ILineDataSet>()
            dataSets.add(lineDataSet)
            val newData = LineData(dataSets)
            statisticsLineChart.data = newData
            statisticsLineChart.invalidate()
            statisticsLineChart.xAxis?.run {
                position = XAxis.XAxisPosition.BOTTOM
                axisMinimum = LINE_CHART_X_AXIS_MINIMUM
                granularity = LINE_CHART_X_AXIS_GRANULARITY
                valueFormatter =
                    IndexAxisValueFormatter(
                        data.keys.toTypedArray().map { item: LocalDateTime ->
                            SimpleDateFormat(
                                TRANSACTION_DATE_FORMAT_SHORT,
                                Locale.getDefault()
                            ).format(item.toMillis())
                        }
                    )
            }
        }
    }

    private fun updatePeriod(period: StatisticsPeriod = StatisticsPeriod.MONTH) {
        val oldView = getViewFromPeriod(viewModel.chartPeriod)
        oldView?.isSelected = false
        val newView = getViewFromPeriod(period)
        newView?.isSelected = true
        viewModel.updatePeriod(period)
        updatePeriod()
    }

    private fun getViewFromPeriod(period: StatisticsPeriod): TextView? {
        return viewBinding?.run {
            when (period) {
                StatisticsPeriod.DAY -> statisticsDay
                StatisticsPeriod.WEEK -> statisticsWeek
                StatisticsPeriod.MONTH -> statisticsMonth
                StatisticsPeriod.YEAR -> statisticsYear
                is StatisticsPeriod.CUSTOM -> statisticsCustom
            }
        }
    }

    private fun showViewStub() {
        emptyViewStub?.root?.show()
        viewBinding?.run {
            statisticsDetailedInfo.gone()
            hideChart()
        }
    }

    private fun hideViewStub() {
        emptyViewStub?.root?.gone()
        viewBinding?.run {
            statisticsDetailedInfo.show()
            showChart()
        }
    }

    companion object {
        private val PERCENT_LIMIT_TO_SHOW_LABEL = BigDecimal(6)
        private const val MAX_CATEGORY_NAME_LENGTH = 10
        private const val ANIMATION_DURATION = 500
        private const val HIDE_MENU_DELAY = 5000L

        private const val LINE_CHART_MAX_VISIBLE_VALUE_COUNT = 20
        private const val LINE_CHART_LINE_LENGTH = 10f
        private const val LINE_CHART_SPACE_LENGTH = 5f
        private const val LINE_CHART_SPACE_LENGTH_AXIS = 10f
        private const val LINE_CHART_PHASE = 0f
        private const val LINE_CHART_X_AXIS_MINIMUM = 0f
        private const val LINE_CHART_X_AXIS_GRANULARITY = 1f
        private const val LINE_CHART_DATA_LABEL_TEXT_SIZE = 9f
        private const val LINE_CHART_DATA_FORM_SIZE = 15f
        private const val LINE_CHART_DATA_CIRCLE_RADIUS = 1f

        private const val PIE_CHART_LEFT_OFFSET = 10f
        private const val PIE_CHART_RIGHT_OFFSET = 10f
        private const val PIE_CHART_TOP_OFFSET = 0f
        private const val PIE_CHART_BOTTOM_OFFSET = 0f
        private const val PIE_CHART_VALUE_TEXT_SIZE = 12f
        private const val PIE_CHART_SLICE_SPACE = 3f
        private const val PIE_CHART_ICONS_X_OFFSET = 0f
        private const val PIE_CHART_ICONS_Y_OFFSET = 40f
        private const val PIE_CHART_SELECTION_SHIFT = 5f
        private const val PIE_CHART_VALUE_LINE_PART_OFFSET_PERCENTAGE = 80f
        private const val PIE_CHART_VALUE_LINE_PART1_LENGTH = 0.2f
        private const val PIE_CHART_VALUE_LINE_PART2_LENGTH = 0.4f
        private const val PIE_CHART_CENTER_TEXT_SIZE = 16f
        private const val PIE_CHART_ENTRY_LABEL_TEXT_SIZE = 15f
        private const val PIE_CHART_ROTATION_ANGEL = 0f
        private const val PIE_CHART_RADIUS = 48f
        private const val PIE_CHART_DRAG_DECELERATION_FRICTION_COEF = 0.95f
    }
}
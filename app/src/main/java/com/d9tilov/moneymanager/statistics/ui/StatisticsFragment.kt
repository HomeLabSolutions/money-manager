package com.d9tilov.moneymanager.statistics.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.RECENT_DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.isSameDay
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.toMillis
import com.d9tilov.moneymanager.databinding.FragmentStatisticsBinding
import com.d9tilov.moneymanager.databinding.LayoutEmptyStatisticsPlaceholderBinding
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsChartMode
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsChartMode.LINE_CHART
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsChartMode.PIE_CHART
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod.CUSTOM
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod.DAY
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod.MONTH
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod.WEEK
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod.YEAR
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class StatisticsFragment :
    BaseFragment<StatisticsNavigator>(R.layout.fragment_statistics),
    StatisticsNavigator,
    OnChartValueSelectedListener {

    override fun getNavigator() = this
    override val viewModel by viewModels<StatisticsViewModel>()
    private val viewBinding by viewBinding(FragmentStatisticsBinding::bind)
    private var emptyViewStub: LayoutEmptyStatisticsPlaceholderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChartMode(viewModel.chartMode, viewModel.chartPeriod)
        viewBinding.run {
            emptyViewStub = viewBinding.statisticsEmptyPlaceholder
            statisticsDay.setOnClickListener { setChartMode(PIE_CHART, DAY) }
            statisticsWeek.setOnClickListener { setChartMode(PIE_CHART, WEEK) }
            statisticsMonth.setOnClickListener { setChartMode(PIE_CHART, MONTH) }
            statisticsYear.setOnClickListener { setChartMode(PIE_CHART, YEAR) }
            statisticsCustom.setOnClickListener { setChartMode(PIE_CHART, CUSTOM) }
        }

        initChart()
        viewModel.getTransactions().observe(viewLifecycleOwner) { list -> setData(list) }
    }

    private fun updatePeriod() {
        val to = currentDate()
        val from = when (viewModel.chartPeriod) {
            DAY -> to
            WEEK -> to.minus(1, DateTimeUnit.WEEK)
            MONTH -> to.minus(1, DateTimeUnit.MONTH)
            YEAR -> to.minus(1, DateTimeUnit.YEAR)
            CUSTOM -> to.minus(1, DateTimeUnit.YEAR)
        }

        viewBinding.statisticsPieChart.centerText =
            if (to.isSameDay(from)) getString(R.string.statistics_today)
            else SimpleDateFormat(RECENT_DATE_FORMAT, Locale.getDefault()).format(Date(from.getEndOfDay().toMillis())) +
                " - " + SimpleDateFormat(RECENT_DATE_FORMAT, Locale.getDefault()).format(Date(to.getEndOfDay().toMillis()))
        viewBinding.statisticsPieChart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.control_activated_color))
    }

    private fun setData(list: List<TransactionChartModel>) {
        if (list.isEmpty()) {
            showViewStub()
            return
        }
        hideViewStub()
        val sum = list.sumOf { item -> item.usdSum }
        val dataSet = PieDataSet(
            list
                .filter { tr ->
                    tr.usdSum.divideBy(sum)
                        .multiply(BigDecimal(100)) > PERCENT_LIMIT_TO_SHOW_LABEL_FILTER
                }
                .map { tr ->
                    val categoryName = tr.category.name
                    val percent: BigDecimal = tr.usdSum.divideBy(sum).multiply(BigDecimal(100))
                    val displayName = if (percent > PERCENT_LIMIT_TO_SHOW_LABEL) {
                        if (categoryName.length > MAX_CATEGORY_NAME_LENGTH) "${
                            categoryName.dropLast(
                                categoryName.length - MAX_CATEGORY_NAME_LENGTH
                            )
                        }..." else categoryName
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
        dataSet.valueLineColor = ContextCompat.getColor(requireContext(), R.color.primary_color)

        // add a lot of colors
        dataSet.colors = list.map { ContextCompat.getColor(requireContext(), it.category.color) }
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(viewBinding.statisticsPieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
        viewBinding.statisticsPieChart.data = data

        updatePeriod()
        viewBinding.statisticsPieChart.highlightValues(null)
        viewBinding.statisticsPieChart.invalidate()
    }

    private fun initChart() {
        val tfRegular = Typeface.createFromAsset(requireContext().assets, "OpenSans-Regular.ttf")
        viewBinding.run {
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
            statisticsPieChart.animateY(1400, Easing.EaseInOutQuad)
            statisticsPieChart.setEntryLabelColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            statisticsPieChart.setEntryLabelTypeface(tfRegular)
            statisticsPieChart.setEntryLabelTextSize(15f)
            statisticsPieChart.setCenterTextSize(16f)

            val l: Legend = statisticsPieChart.legend
            l.isEnabled = false
        }
    }

    private fun setChartMode(
        mode: StatisticsChartMode,
        period: StatisticsPeriod = MONTH
    ) {
        when (mode) {
            PIE_CHART -> {
                val oldView = getViewFromPeriod(viewModel.chartPeriod)
                oldView.isSelected = false
                val newView = getViewFromPeriod(period)
                newView.isSelected = true
                viewModel.updatePeriod(period)
            }
            LINE_CHART -> {}
        }
    }

    private fun getViewFromPeriod(period: StatisticsPeriod): TextView {
        return viewBinding.run {
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
        private val PERCENT_LIMIT_TO_SHOW_LABEL_FILTER = BigDecimal(0)
        private val PERCENT_LIMIT_TO_SHOW_LABEL = BigDecimal(3)
        private const val MAX_CATEGORY_NAME_LENGTH = 10
    }
}

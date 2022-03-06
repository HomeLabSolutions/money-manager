package com.d9tilov.moneymanager.statistics.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.databinding.FragmentStatisticsBinding
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment :
    BaseFragment<StatisticsNavigator>(R.layout.fragment_statistics),
    StatisticsNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<StatisticsViewModel>()
    private val viewBinding by viewBinding(FragmentStatisticsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChartMode(viewModel.chartMode, viewModel.chartPeriod)
        viewBinding.run {
            statisticsDay.setOnClickListener { setChartMode(PIE_CHART, DAY) }
            statisticsWeek.setOnClickListener { setChartMode(PIE_CHART, WEEK) }
            statisticsMonth.setOnClickListener { setChartMode(PIE_CHART, MONTH) }
            statisticsYear.setOnClickListener { setChartMode(PIE_CHART, YEAR) }
            statisticsCustom.setOnClickListener { setChartMode(PIE_CHART, CUSTOM) }
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
                viewModel.chartPeriod = period
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
}

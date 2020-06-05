package com.d9tilov.moneymanager.statistics.ui

import android.view.View
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.databinding.FragmentStatisticsBinding
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel

class StatisticsFragment :
    BaseFragment<FragmentStatisticsBinding, StatisticsNavigator, StatisticsViewModel>(),
    StatisticsNavigator {

    override fun performDataBinding(view: View): FragmentStatisticsBinding =
        FragmentStatisticsBinding.bind(view)

    override fun getLayoutId() = R.layout.fragment_statistics
    override fun getViewModelClass() = StatisticsViewModel::class.java
    override fun getNavigator() = this
}

package com.d9tilov.moneymanager.statistics.ui

import android.view.View
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.databinding.FragmentStatisticsBinding
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import javax.inject.Inject

class StatisticsFragment : BaseFragment<FragmentStatisticsBinding, StatisticsViewModel>(),
    StatisticsNavigator {

    @Inject
    internal lateinit var viewModel: StatisticsViewModel

    override fun performDataBinding(view: View): FragmentStatisticsBinding =
        FragmentStatisticsBinding.bind(view)

    override fun getLayoutId() = R.layout.fragment_statistics
    override fun getViewModel() = viewModel
    override fun getNavigator() = this
}

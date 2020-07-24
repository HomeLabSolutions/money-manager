package com.d9tilov.moneymanager.statistics.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.databinding.FragmentStatisticsBinding
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment :
    BaseFragment<FragmentStatisticsBinding, StatisticsNavigator>(R.layout.fragment_statistics),
    StatisticsNavigator {

    override fun performDataBinding(view: View): FragmentStatisticsBinding =
        FragmentStatisticsBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<StatisticsViewModel>()
}

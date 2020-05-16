package com.d9tilov.moneymanager.statistics.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.statistics.di.inject
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import javax.inject.Inject

class StatisticsFragment : Fragment(R.layout.fragment_statistics), StatisticsNavigator {

    @Inject
    internal lateinit var viewModel: StatisticsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        viewModel.navigator = this
    }
}

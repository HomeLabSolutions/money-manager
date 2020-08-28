package com.d9tilov.moneymanager.prepopulate.goals.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.databinding.FragmentGoalsBinding
import com.d9tilov.moneymanager.prepopulate.goals.vm.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment :
    BaseFragment<FragmentGoalsBinding, GoalsNavigator>(R.layout.fragment_goals),
    GoalsNavigator {
    override fun performDataBinding(view: View) = FragmentGoalsBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<GoalsViewModel>()
}

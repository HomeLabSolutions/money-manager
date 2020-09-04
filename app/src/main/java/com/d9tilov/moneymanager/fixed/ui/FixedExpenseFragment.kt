package com.d9tilov.moneymanager.fixed.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.FixedExpenseNavigator
import com.d9tilov.moneymanager.databinding.FragmentFixedExpenseBinding
import com.d9tilov.moneymanager.fixed.vm.FixedExpenseViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedExpenseFragment :
    BaseFragment<FragmentFixedExpenseBinding, FixedExpenseNavigator>(R.layout.fragment_fixed_expense),
    FixedExpenseNavigator {

    private var toolbar: MaterialToolbar? = null

    override fun performDataBinding(view: View) = FragmentFixedExpenseBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<FixedExpenseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.prepopulate_menu, menu)
        menu.findItem(R.id.action_skip).isVisible = false
        menu.findItem(R.id.action_add).isVisible = true
    }

    private fun initToolbar() {
        toolbar = viewBinding?.fixedExpenseToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_prepopulate_fixed_income)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                }
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }
}

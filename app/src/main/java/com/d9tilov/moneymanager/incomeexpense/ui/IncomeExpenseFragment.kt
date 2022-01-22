package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.core.events.OnBackPressed
import com.d9tilov.moneymanager.core.events.OnDialogDismissListener
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.databinding.FragmentIncomeExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter.Companion.TAB_COUNT
import com.d9tilov.moneymanager.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.ui.TransactionRemoveDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncomeExpenseFragment :
    BaseFragment<IncomeExpenseNavigator>(R.layout.fragment_income_expense),
    IncomeExpenseNavigator,
    OnBackPressed {

    private val viewBinding by viewBinding(FragmentIncomeExpenseBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeExpenseViewModel>()

    private lateinit var incomeExpenseAdapter: IncomeExpenseAdapter
    private var pageIndex = 0

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomeExpenseAdapter = IncomeExpenseAdapter(childFragmentManager, lifecycle)
        viewBinding.incomeExpenseViewPager.adapter = incomeExpenseAdapter
        viewBinding.run {
            incomeExpenseViewPager
                .registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        pageIndex = position
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                            param(
                                FirebaseAnalytics.Param.ITEM_ID,
                                if (position == 0) "expense" else "income"
                            )
                        }
                    }
                })

            TabLayoutMediator(
                viewBinding.incomeExpenseTabLayout,
                viewBinding.incomeExpenseViewPager
            ) { tab, position ->
                tab.text = when (position) {
                    0 -> requireContext().getString(R.string.tab_expense)
                    else -> requireContext().getString(R.string.tab_income)
                }
            }.attach()
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            TransactionRemoveDialog.ARG_UNDO_REMOVE_LAYOUT_DISMISS
        )?.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                val fragments = getFragmentsInViewPager()
                for (fragment in fragments) {
                    (fragment as? OnDialogDismissListener)?.onDismiss()
                }
            }
        }
    }

    private fun getFragmentsInViewPager(): List<Fragment> {
        val fragments = mutableListOf<Fragment>()
        for (i in 0 until TAB_COUNT) {
            val fragment = childFragmentManager.findFragmentByTag(
                "f" + viewBinding.incomeExpenseViewPager.adapter?.getItemId(i)
            )
            fragment?.let { fragments.add(it) }
        }
        return fragments
    }

    override fun onBackPressed(): Boolean {
        val fragment = getFragmentsInViewPager().getOrNull(pageIndex)
        return (fragment as? OnBackPressed)?.onBackPressed() ?: true
    }

    companion object {
        const val ARG_TRANSACTION_CREATED = "arg_transaction_created"
    }
}

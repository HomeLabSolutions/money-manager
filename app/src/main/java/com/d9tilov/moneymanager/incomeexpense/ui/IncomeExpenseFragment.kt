package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.databinding.FragmentIncomeExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter.Companion.TAB_COUNT
import com.d9tilov.moneymanager.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncomeExpenseFragment :
    BaseFragment<IncomeExpenseNavigator>(R.layout.fragment_income_expense),
    IncomeExpenseNavigator,
    OnKeyboardVisibleChange {

    private val args by navArgs<IncomeExpenseFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val viewBinding by viewBinding(FragmentIncomeExpenseBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeExpenseViewModel>()

    private lateinit var incomeExpenseAdapter: IncomeExpenseAdapter
    private var currentPage = 0

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
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                            param(
                                FirebaseAnalytics.Param.ITEM_ID,
                                if (position == 0) "expense" else "income"
                            )
                        }
                        currentPage = position

                        if (countFragmentsInViewPager() != TAB_COUNT) return
                        val currentFragment = getCurrentPagedFragment()
                        if ((activity as BaseActivity<*>).isKeyboardShown) {
                            currentFragment?.onOpenKeyboard()
                        } else {
                            currentFragment?.onCloseKeyboard()
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
            incomeExpenseViewPager.currentItem =
                (if (transactionType == TransactionType.INCOME) 1 else 0)
        }
    }

    private fun countFragmentsInViewPager(): Int {
        val fragments = mutableListOf<Fragment>()
        for (i in 0 until TAB_COUNT) {
            val fragment = childFragmentManager.findFragmentByTag(
                "f" + viewBinding.incomeExpenseViewPager.adapter?.getItemId(i)
            )
            fragment?.let { fragments.add(it) }
        }
        return fragments.size
    }

    override fun onOpenKeyboard() {
        getCurrentPagedFragment()?.onOpenKeyboard()
    }

    override fun onCloseKeyboard() {
        getCurrentPagedFragment()?.onCloseKeyboard()
    }

    private fun getCurrentPagedFragment(): OnKeyboardVisibleChange? =
        childFragmentManager.findFragmentByTag(
            "f" + viewBinding.incomeExpenseViewPager.adapter?.getItemId(currentPage)
        ) as? OnKeyboardVisibleChange

    companion object {
        const val ARG_TRANSACTION_CREATED = "arg_transaction_created"
    }
}

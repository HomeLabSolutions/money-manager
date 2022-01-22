package com.d9tilov.moneymanager.incomeexpense.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentIncomeExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter
import com.d9tilov.moneymanager.incomeexpense.ui.adapter.IncomeExpenseAdapter.Companion.TAB_COUNT
import com.d9tilov.moneymanager.incomeexpense.ui.listeners.OnIncomeExpenseListener
import com.d9tilov.moneymanager.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.moneymanager.keyboard.PinButton
import com.d9tilov.moneymanager.keyboard.PinKeyboard
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
    private var pageIndex = -1
    private var isKeyboardOpen = true

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomeExpenseAdapter = IncomeExpenseAdapter(childFragmentManager, lifecycle)
        viewBinding.run {
            incomeExpenseViewPager.adapter = incomeExpenseAdapter
            incomeExpenseViewPager.offscreenPageLimit = 2
            incomeExpenseViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    pageIndex = position
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                        param(
                            FirebaseAnalytics.Param.ITEM_ID,
                            if (position == 0) "expense" else "income"
                        )
                    }
                    if (isKeyboardOpen) openKeyboard()
                    else closeKeyboard()
                }
            })

            TabLayoutMediator(incomeExpenseTabLayout, incomeExpenseViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> requireContext().getString(R.string.tab_expense)
                    else -> requireContext().getString(R.string.tab_income)
                }
            }.attach()
            incomeExpenseKeyboardLayout.btnHideKeyboard.setOnClickListener {
                closeKeyboard()
            }
            incomeExpenseKeyboardLayout.pinKeyboard.clickPinButton = object : PinKeyboard.ClickPinButton {
                override fun onPinClick(button: PinButton?) {
                    val fragment = (getCurrentFragment() as? OnIncomeExpenseListener)
                    when (button?.id) {
                        R.id.keyboard_pin_0 -> fragment?.onHandleInput(getString(R.string._0))
                        R.id.keyboard_pin_1 -> fragment?.onHandleInput(getString(R.string._1))
                        R.id.keyboard_pin_2 -> fragment?.onHandleInput(getString(R.string._2))
                        R.id.keyboard_pin_3 -> fragment?.onHandleInput(getString(R.string._3))
                        R.id.keyboard_pin_4 -> fragment?.onHandleInput(getString(R.string._4))
                        R.id.keyboard_pin_5 -> fragment?.onHandleInput(getString(R.string._5))
                        R.id.keyboard_pin_6 -> fragment?.onHandleInput(getString(R.string._6))
                        R.id.keyboard_pin_7 -> fragment?.onHandleInput(getString(R.string._7))
                        R.id.keyboard_pin_8 -> fragment?.onHandleInput(getString(R.string._8))
                        R.id.keyboard_pin_9 -> fragment?.onHandleInput(getString(R.string._9))
                        R.id.keyboard_pin_dot -> fragment?.onHandleInput(getString(R.string._dot))
                        else -> fragment?.onHandleInput(null)
                    }
                }
            }
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            TransactionRemoveDialog.ARG_UNDO_REMOVE_LAYOUT_DISMISS
        )?.observe(viewLifecycleOwner) {
            if (it) {
                val fragments = getFragmentsInViewPager()
                for (fragment in fragments) (fragment as? OnDialogDismissListener)?.onDismiss()
            }
        }
    }

    fun openKeyboard() {
        isKeyboardOpen = true
        if (!viewBinding.incomeExpenseKeyboardLayout.root.isVisible) {
            viewBinding.incomeExpenseKeyboardLayout.root.apply {
                alpha = 0f
                show()
                animate()
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(null)
            }
        }
        for (fragment in getFragmentsInViewPager()) {
            (fragment as? OnIncomeExpenseListener)?.onKeyboardShown(true)
        }
    }

    fun closeKeyboard() {
        isKeyboardOpen = false
        viewBinding.incomeExpenseKeyboardLayout.root.gone()
        for (fragment in getFragmentsInViewPager()) {
            (fragment as? OnIncomeExpenseListener)?.onKeyboardShown(false)
        }
    }

    fun isKeyboardOpened(): Boolean = isKeyboardOpen

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

    private fun getCurrentFragment(): Fragment? = getFragmentsInViewPager().getOrNull(pageIndex)

    override fun onBackPressed(): Boolean {
        return if (isKeyboardOpen) {
            closeKeyboard()
            false
        } else {
            true
        }
    }

    companion object {
        const val ARG_TRANSACTION_CREATED = "arg_transaction_created"
        const val ANIMATION_DURATION = 300L
    }
}

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
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideWithAnimation
import com.d9tilov.moneymanager.core.util.showWithAnimation
import com.d9tilov.moneymanager.currency.CurrencyDestination
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.ui.CurrencyFragment
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
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class IncomeExpenseFragment :
    BaseFragment<IncomeExpenseNavigator, FragmentIncomeExpenseBinding>(FragmentIncomeExpenseBinding::inflate, R.layout.fragment_income_expense),
    IncomeExpenseNavigator,
    OnBackPressed {

    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeExpenseViewModel>()

    private var incomeExpenseAdapter: IncomeExpenseAdapter? = null
    private var pageIndex = -1
    private var isKeyboardOpen = true
    private val commonGroup = mutableListOf<View>()

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomeExpenseAdapter = IncomeExpenseAdapter(childFragmentManager, lifecycle) // must create adapter every time
        viewBinding?.run {
            if (commonGroup.isEmpty()) {
                commonGroup.add(incomeExpenseMainSum)
                commonGroup.add(incomeExpenseKeyboardLayout.root)
            }
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
                    when (button?.id) {
                        R.id.keyboard_pin_0 -> onHandleInput(getString(R.string._0))
                        R.id.keyboard_pin_1 -> onHandleInput(getString(R.string._1))
                        R.id.keyboard_pin_2 -> onHandleInput(getString(R.string._2))
                        R.id.keyboard_pin_3 -> onHandleInput(getString(R.string._3))
                        R.id.keyboard_pin_4 -> onHandleInput(getString(R.string._4))
                        R.id.keyboard_pin_5 -> onHandleInput(getString(R.string._5))
                        R.id.keyboard_pin_6 -> onHandleInput(getString(R.string._6))
                        R.id.keyboard_pin_7 -> onHandleInput(getString(R.string._7))
                        R.id.keyboard_pin_8 -> onHandleInput(getString(R.string._8))
                        R.id.keyboard_pin_9 -> onHandleInput(getString(R.string._9))
                        R.id.keyboard_pin_dot -> onHandleInput(getString(R.string._dot))
                        else -> onHandleInput(null)
                    }
                }
            }
            incomeExpenseMainSum.setOnClickListener {
                val action =
                    IncomeExpenseFragmentDirections.toCurrencyDest(
                        CurrencyDestination.INCOME_EXPENSE_SCREEN,
                        viewModel.getCurrencyCode()
                    )
                findNavController().navigate(action)
            }
        }
        viewModel.getCurrencyCodeAsync().observe(
            viewLifecycleOwner
        ) { viewBinding?.incomeExpenseMainSum?.setCurrencyCode(it) }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DomainCurrency>(
            CurrencyFragment.ARG_CURRENCY
        )?.observe(
            viewLifecycleOwner
        ) {
            viewModel.setCurrencyCode(it.code)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<DomainCurrency>(
                CurrencyFragment.ARG_CURRENCY
            )
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

    private fun onHandleInput(str: String?) {
        val dot = getString(R.string._dot)
        val zero = getString(R.string._0)
        var input = viewBinding?.incomeExpenseMainSum?.moneyEditText?.text.toString()
        when (str) {
            dot -> {
                if (input.contains(dot)) return
                if (input.isEmpty() || input[input.length - 1].toString() == dot) return
                else input = input.plus(str)
            }
            zero -> {
                if (input.length == 1 && input[0].toString() == zero) return
                else input = input.plus(str)
            }
            null ->
                if (input.isNotEmpty()) input = input.removeRange(input.length - 1, input.length)
            else -> input = input.plus(str)
        }
        if (input.isEmpty()) input = getString(R.string._0)
        viewBinding?.incomeExpenseMainSum?.moneyEditText?.setText(input)
    }

    fun openKeyboard() {
        isKeyboardOpen = true
        viewBinding?.run {
            if (pageIndex == 0) {
                incomeExpenseMainSumTitleRight.showWithAnimation()
                incomeExpenseMainSumTitleLeft.hideWithAnimation()
            } else {
                incomeExpenseMainSumTitleLeft.showWithAnimation()
                incomeExpenseMainSumTitleRight.hideWithAnimation()
            }
        }
        if (commonGroup.isNotEmpty() && !commonGroup[0].isVisible) {
            commonGroup.forEach { it.showWithAnimation() }
        }
        for (fragment in getFragmentsInViewPager()) {
            (fragment as? OnIncomeExpenseListener)?.onKeyboardShown(true)
        }
    }

    fun closeKeyboard() {
        isKeyboardOpen = false
        commonGroup.forEach { it.gone() }
        viewBinding?.incomeExpenseMainSumTitleLeft?.gone()
        viewBinding?.incomeExpenseMainSumTitleRight?.gone()
        for (fragment in getFragmentsInViewPager()) {
            (fragment as? OnIncomeExpenseListener)?.onKeyboardShown(false)
        }
    }

    fun isKeyboardOpened(): Boolean = isKeyboardOpen

    fun getSum(): BigDecimal = viewBinding?.incomeExpenseMainSum?.getValue() ?: BigDecimal.ZERO
    fun getCurrencyCode(): String = viewModel.getCurrencyCode()

    fun resetSum() {
        viewBinding?.incomeExpenseMainSum?.setValue(BigDecimal.ZERO, viewModel.getCurrencyCode())
        viewModel.setDefaultCurrencyCode()
        onHandleInput("")
    }

    private fun getFragmentsInViewPager(): List<Fragment> {
        val fragments = mutableListOf<Fragment>()
        for (i in 0 until TAB_COUNT) {
            val fragment = childFragmentManager.findFragmentByTag(
                "f" + viewBinding?.incomeExpenseViewPager?.adapter?.getItemId(i)
            )
            fragment?.let { fragments.add(it) }
        }
        return fragments
    }

    override fun onBackPressed(): Boolean {
        return if (isKeyboardOpen) {
            closeKeyboard()
            false
        } else {
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        commonGroup.clear()
    }

    companion object {
        const val ARG_TRANSACTION_CREATED = "arg_transaction_created"
    }
}

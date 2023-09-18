package com.d9tilov.android.incomeexpense.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.common.android.utils.gone
import com.d9tilov.android.common.android.utils.hideWithAnimation
import com.d9tilov.android.common.android.utils.showWithAnimation
import com.d9tilov.android.core.events.OnBackPressed
import com.d9tilov.android.core.events.OnDialogDismissListener
import com.d9tilov.android.incomeexpense.keyboard.PinButton
import com.d9tilov.android.incomeexpense.keyboard.PinKeyboard
import com.d9tilov.android.incomeexpense.navigation.IncomeExpenseNavigator
import com.d9tilov.android.incomeexpense.ui.adapter.IncomeExpenseAdapter
import com.d9tilov.android.incomeexpense.ui.adapter.IncomeExpenseAdapter.Companion.TAB_COUNT
import com.d9tilov.android.incomeexpense.ui.listeners.OnIncomeExpenseListener
import com.d9tilov.android.incomeexpense_ui.R
import com.d9tilov.android.incomeexpense_ui.databinding.FragmentIncomeExpenseBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class IncomeExpenseFragment :
    BaseFragment<IncomeExpenseNavigator, FragmentIncomeExpenseBinding>(FragmentIncomeExpenseBinding::inflate, R.layout.fragment_income_expense),
    IncomeExpenseNavigator,
    OnBackPressed {

    override fun getNavigator() = this
    override val viewModel: BaseViewModel<IncomeExpenseNavigator>
        get() = TODO("Not yet implemented")
//    override val viewModel by viewModels<IncomeExpenseViewModel>()

    private var incomeExpenseAdapter: IncomeExpenseAdapter? = null
    private var pageIndex = -1
    private val commonGroup = mutableListOf<View>()
    var isKeyboardOpen = true
        private set
    var isPremium = false
        private set

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomeExpenseAdapter =
            IncomeExpenseAdapter(
                childFragmentManager,
                lifecycle
            ) // must create adapter every time
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
                        param(FirebaseAnalytics.Param.ITEM_ID, if (position == 0) "expense" else "income")
                    }
                    if (isKeyboardOpen) {
                        openKeyboard()
                    } else {
                        closeKeyboard()
                    }
                }
            })

            TabLayoutMediator(incomeExpenseTabLayout, incomeExpenseViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> requireContext().getString(R.string.tab_expense)
                    else -> requireContext().getString(R.string.tab_income)
                }
            }.attach()
            incomeExpenseKeyboardLayout.btnHideKeyboard.setOnClickListener { closeKeyboard() }
            incomeExpenseKeyboardLayout.pinKeyboard.clickPinButton =
                object : PinKeyboard.ClickPinButton {
                    override fun onPinClick(button: PinButton?) {
                        when (button?.id) {
                            R.id.keyboard_pin_0 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._0))
                            R.id.keyboard_pin_1 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._1))
                            R.id.keyboard_pin_2 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._2))
                            R.id.keyboard_pin_3 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._3))
                            R.id.keyboard_pin_4 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._4))
                            R.id.keyboard_pin_5 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._5))
                            R.id.keyboard_pin_6 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._6))
                            R.id.keyboard_pin_7 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._7))
                            R.id.keyboard_pin_8 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._8))
                            R.id.keyboard_pin_9 -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._9))
                            R.id.keyboard_pin_dot -> onHandleInput(getString(com.d9tilov.android.common.android.R.string._dot))
                            else -> onHandleInput(null)
                        }
                    }
                }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
//                        viewModel.isPremium.collect { isPremium ->
//                            this@IncomeExpenseFragment.isPremium = isPremium
//                        }
                    }
                    launch {
//                        viewModel.getCurrencyCodeAsync()
//                            .collect {
//                                incomeExpenseMainSum.setValue(
//                                    incomeExpenseMainSum.getValue(),
//                                    it
//                                )
//                            }
                    }
                }
            }
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.run {
            getLiveData<Boolean>(com.d9tilov.android.transaction.ui.TransactionRemoveDialog.ARG_UNDO_REMOVE_LAYOUT_DISMISS)
                .observe(viewLifecycleOwner) {
                    if (it) {
                        val fragments = getFragmentsInViewPager()
                        for (fragment in fragments) (fragment as? OnDialogDismissListener)?.onDismiss()
                    }
                }
        }
    }

    private fun onHandleInput(str: String?) {
        val dot = getString(com.d9tilov.android.common.android.R.string._dot)
        val zero = getString(com.d9tilov.android.common.android.R.string._0)
        var input = viewBinding?.incomeExpenseMainSum?.moneyEditText?.text.toString()
        when (str) {
            dot -> {
                if (input.contains(dot) ||
                    input.isEmpty() ||
                    input[input.length - 1].toString() == dot
                ) {
                    return
                } else {
                    input = input.plus(str)
                }
            }
            zero -> {
                if (input.length == 1 && input[0].toString() == zero) {
                    return
                } else {
                    input = input.plus(str)
                }
            }
            null ->
                if (input.isNotEmpty()) {
                    input =
                        input.removeRange(input.length - 1, input.length)
                }
            else -> input = input.plus(str)
        }
        if (input.isEmpty()) input = getString(com.d9tilov.android.common.android.R.string._0)
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
//    fun getCurrencyCode(): String = viewModel.getCurrencyCode()

    fun resetSum() {
//        viewBinding?.incomeExpenseMainSum?.setValue(
//            BigDecimal.ZERO,
//            viewModel.getCurrencyCode()
//        )
//        viewModel.setDefaultCurrencyCode()
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
}

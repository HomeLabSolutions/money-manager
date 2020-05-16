package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinButton
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinKeyboard
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import kotlinx.android.synthetic.main.keyboard_layout.view.*
import javax.inject.Inject

class ExpenseFragment : BaseFragment<FragmentExpenseBinding, ExpenseViewModel>(), ExpenseNavigator {

    @Inject
    internal lateinit var viewModel: ExpenseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.transactionMainContent.pin_keyboard.setClickPinButton(PinClickListener())
    }

    inner class PinClickListener : PinKeyboard.ClickPinButton {
        override fun onPinClick(pinButton: PinButton) {
            when (val child = pinButton.getChildAt(0)) {
                is TextView -> {
                    val digit = child.text.toString()
                    viewModel.updateNum(viewBinding.expenseMainSum.getSum())
                    viewBinding.expenseMainSum.setSum(viewModel.tapNum(digit))

                }
                is ImageView -> {
                    viewModel.updateNum(viewBinding.expenseMainSum.getSum())
                    viewBinding.expenseMainSum.setSum(viewModel.removeNum())
                }
            }
        }
    }

    override fun performDataBinding(view: View): FragmentExpenseBinding =
        FragmentExpenseBinding.bind(view)

    override fun getLayoutId() = R.layout.fragment_expense
    override fun getViewModel() = viewModel
    override fun getNavigator() = this
}
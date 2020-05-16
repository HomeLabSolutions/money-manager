package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinButton
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinKeyboard
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.expense.di.inject
import kotlinx.android.synthetic.main.keyboard_layout.view.*
import javax.inject.Inject

class ExpenseFragment : Fragment(R.layout.fragment_expense), ExpenseNavigator {

    @Inject
    internal lateinit var viewModel: ExpenseViewModel
    private lateinit var binding: FragmentExpenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        viewModel.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpenseBinding.bind(view)
        binding.transactionMainContent.pin_keyboard.setClickPinButton(PinClickListener())
    }

    inner class PinClickListener : PinKeyboard.ClickPinButton {
        override fun onPinClick(pinButton: PinButton) {
            when (val child = pinButton.getChildAt(0)) {
                is TextView -> {
                    val digit = child.text.toString()
                    viewModel.updateNum(binding.expenseMainSum.getSum())
                    binding.expenseMainSum.setSum(viewModel.tapNum(digit))

                }
                is ImageView -> {
                    viewModel.updateNum(binding.expenseMainSum.getSum())
                    binding.expenseMainSum.setSum(viewModel.removeNum())
                }
            }
        }
    }
}
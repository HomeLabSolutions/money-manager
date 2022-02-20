package com.d9tilov.moneymanager.regular.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.DayOfMonthDialogNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.databinding.FragmentDialogDayOfMonthBinding
import com.d9tilov.moneymanager.regular.ui.RegularTransactionCreationFragment.Companion.ARG_DAY_OF_MONTH
import com.d9tilov.moneymanager.regular.vm.DayOfMonthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayOfMonthDialog :
    BaseDialogFragment<DayOfMonthDialogNavigator>(), DayOfMonthDialogNavigator {

    private val viewBinding by viewBinding(FragmentDialogDayOfMonthBinding::bind)

    override val layoutId = R.layout.fragment_dialog_day_of_month
    override fun getNavigator() = this
    override val viewModel by viewModels<DayOfMonthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDayOfMonth(viewModel.dayOfMonth)
        viewBinding.run {
            dayOfMonthButtonConfirm.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ARG_DAY_OF_MONTH,
                    viewModel.dayOfMonth
                )
                dismiss()
            }
            dayOfMonthButtonCancel.setOnClickListener { dismiss() }
            dayOfMonth1.setOnClickListener { setDayOfMonth(1) }
            dayOfMonth2.setOnClickListener { setDayOfMonth(2) }
            dayOfMonth3.setOnClickListener { setDayOfMonth(3) }
            dayOfMonth4.setOnClickListener { setDayOfMonth(4) }
            dayOfMonth5.setOnClickListener { setDayOfMonth(5) }
            dayOfMonth6.setOnClickListener { setDayOfMonth(6) }
            dayOfMonth7.setOnClickListener { setDayOfMonth(7) }
            dayOfMonth8.setOnClickListener { setDayOfMonth(8) }
            dayOfMonth9.setOnClickListener { setDayOfMonth(9) }
            dayOfMonth10.setOnClickListener { setDayOfMonth(10) }
            dayOfMonth11.setOnClickListener { setDayOfMonth(11) }
            dayOfMonth12.setOnClickListener { setDayOfMonth(12) }
            dayOfMonth13.setOnClickListener { setDayOfMonth(13) }
            dayOfMonth14.setOnClickListener { setDayOfMonth(14) }
            dayOfMonth15.setOnClickListener { setDayOfMonth(15) }
            dayOfMonth16.setOnClickListener { setDayOfMonth(16) }
            dayOfMonth17.setOnClickListener { setDayOfMonth(17) }
            dayOfMonth18.setOnClickListener { setDayOfMonth(18) }
            dayOfMonth19.setOnClickListener { setDayOfMonth(19) }
            dayOfMonth20.setOnClickListener { setDayOfMonth(20) }
            dayOfMonth21.setOnClickListener { setDayOfMonth(21) }
            dayOfMonth22.setOnClickListener { setDayOfMonth(22) }
            dayOfMonth23.setOnClickListener { setDayOfMonth(23) }
            dayOfMonth24.setOnClickListener { setDayOfMonth(24) }
            dayOfMonth25.setOnClickListener { setDayOfMonth(25) }
            dayOfMonth26.setOnClickListener { setDayOfMonth(26) }
            dayOfMonth27.setOnClickListener { setDayOfMonth(27) }
            dayOfMonth28.setOnClickListener { setDayOfMonth(28) }
            dayOfMonth29.setOnClickListener { setDayOfMonth(29) }
            dayOfMonth30.setOnClickListener { setDayOfMonth(30) }
            dayOfMonth31.setOnClickListener { setDayOfMonth(31) }
        }
    }

    private fun setDayOfMonth(day: Int) {
        for (i in 1 until 31) {
            viewBinding.run {
                val view = when (i) {
                    1 -> dayOfMonth1
                    2 -> dayOfMonth2
                    3 -> dayOfMonth3
                    4 -> dayOfMonth4
                    5 -> dayOfMonth5
                    6 -> dayOfMonth6
                    7 -> dayOfMonth7
                    8 -> dayOfMonth8
                    9 -> dayOfMonth9
                    10 -> dayOfMonth10
                    11 -> dayOfMonth11
                    12 -> dayOfMonth12
                    13 -> dayOfMonth13
                    14 -> dayOfMonth14
                    15 -> dayOfMonth15
                    16 -> dayOfMonth16
                    17 -> dayOfMonth17
                    18 -> dayOfMonth18
                    19 -> dayOfMonth19
                    20 -> dayOfMonth20
                    21 -> dayOfMonth21
                    22 -> dayOfMonth22
                    23 -> dayOfMonth23
                    24 -> dayOfMonth24
                    25 -> dayOfMonth25
                    26 -> dayOfMonth26
                    27 -> dayOfMonth27
                    28 -> dayOfMonth28
                    29 -> dayOfMonth29
                    30 -> dayOfMonth30
                    31 -> dayOfMonth31
                    else -> throw IllegalArgumentException("Unknown day of week: $day")
                }
                view.isSelected = i == day
                view.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (view.isSelected) R.color.primary_color else R.color.control_activated_color
                    )
                )
            }
        }
        viewModel.dayOfMonth = day
    }
}

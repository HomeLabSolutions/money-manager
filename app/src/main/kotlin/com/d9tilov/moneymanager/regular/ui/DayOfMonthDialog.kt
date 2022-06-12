package com.d9tilov.moneymanager.regular.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.DayOfMonthDialogNavigator
import com.d9tilov.moneymanager.databinding.FragmentDialogDayOfMonthPickerBinding
import com.d9tilov.moneymanager.regular.domain.entity.MonthDays
import com.d9tilov.moneymanager.regular.ui.RegularTransactionCreationFragment.Companion.ARG_DAY_OF_MONTH
import com.d9tilov.moneymanager.regular.vm.DayOfMonthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayOfMonthDialog :
    BaseDialogFragment<DayOfMonthDialogNavigator, FragmentDialogDayOfMonthPickerBinding>(FragmentDialogDayOfMonthPickerBinding::inflate), DayOfMonthDialogNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<DayOfMonthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDayOfMonth(viewModel.dayOfMonth)
        viewBinding?.run {
            dayOfMonthButtonConfirm.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ARG_DAY_OF_MONTH,
                    viewModel.dayOfMonth
                )
                dismiss()
            }
            dayOfMonthButtonCancel.setOnClickListener { dismiss() }
            dayOfMonth1.setOnClickListener { setDayOfMonth(MonthDays.DAY_1.ordinal + 1) }
            dayOfMonth2.setOnClickListener { setDayOfMonth(MonthDays.DAY_2.ordinal + 1) }
            dayOfMonth3.setOnClickListener { setDayOfMonth(MonthDays.DAY_3.ordinal + 1) }
            dayOfMonth4.setOnClickListener { setDayOfMonth(MonthDays.DAY_4.ordinal + 1) }
            dayOfMonth5.setOnClickListener { setDayOfMonth(MonthDays.DAY_5.ordinal + 1) }
            dayOfMonth6.setOnClickListener { setDayOfMonth(MonthDays.DAY_6.ordinal + 1) }
            dayOfMonth7.setOnClickListener { setDayOfMonth(MonthDays.DAY_7.ordinal + 1) }
            dayOfMonth8.setOnClickListener { setDayOfMonth(MonthDays.DAY_8.ordinal + 1) }
            dayOfMonth9.setOnClickListener { setDayOfMonth(MonthDays.DAY_9.ordinal + 1) }
            dayOfMonth10.setOnClickListener { setDayOfMonth(MonthDays.DAY_10.ordinal + 1) }
            dayOfMonth11.setOnClickListener { setDayOfMonth(MonthDays.DAY_11.ordinal + 1) }
            dayOfMonth12.setOnClickListener { setDayOfMonth(MonthDays.DAY_12.ordinal + 1) }
            dayOfMonth13.setOnClickListener { setDayOfMonth(MonthDays.DAY_13.ordinal + 1) }
            dayOfMonth14.setOnClickListener { setDayOfMonth(MonthDays.DAY_14.ordinal + 1) }
            dayOfMonth15.setOnClickListener { setDayOfMonth(MonthDays.DAY_15.ordinal + 1) }
            dayOfMonth16.setOnClickListener { setDayOfMonth(MonthDays.DAY_16.ordinal + 1) }
            dayOfMonth17.setOnClickListener { setDayOfMonth(MonthDays.DAY_17.ordinal + 1) }
            dayOfMonth18.setOnClickListener { setDayOfMonth(MonthDays.DAY_18.ordinal + 1) }
            dayOfMonth19.setOnClickListener { setDayOfMonth(MonthDays.DAY_19.ordinal + 1) }
            dayOfMonth20.setOnClickListener { setDayOfMonth(MonthDays.DAY_20.ordinal + 1) }
            dayOfMonth21.setOnClickListener { setDayOfMonth(MonthDays.DAY_21.ordinal + 1) }
            dayOfMonth22.setOnClickListener { setDayOfMonth(MonthDays.DAY_22.ordinal + 1) }
            dayOfMonth23.setOnClickListener { setDayOfMonth(MonthDays.DAY_23.ordinal + 1) }
            dayOfMonth24.setOnClickListener { setDayOfMonth(MonthDays.DAY_24.ordinal + 1) }
            dayOfMonth25.setOnClickListener { setDayOfMonth(MonthDays.DAY_25.ordinal + 1) }
            dayOfMonth26.setOnClickListener { setDayOfMonth(MonthDays.DAY_26.ordinal + 1) }
            dayOfMonth27.setOnClickListener { setDayOfMonth(MonthDays.DAY_27.ordinal + 1) }
            dayOfMonth28.setOnClickListener { setDayOfMonth(MonthDays.DAY_28.ordinal + 1) }
            dayOfMonth29.setOnClickListener { setDayOfMonth(MonthDays.DAY_29.ordinal + 1) }
            dayOfMonth30.setOnClickListener { setDayOfMonth(MonthDays.DAY_30.ordinal + 1) }
            dayOfMonth31.setOnClickListener { setDayOfMonth(MonthDays.DAY_31.ordinal + 1) }
        }
    }

    private fun setDayOfMonth(day: Int) {
        val newView = getViewFromDay(day)
        val oldView = getViewFromDay(viewModel.dayOfMonth)
        oldView?.isSelected = false
        oldView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.control_activated_color))
        newView?.isSelected = true
        newView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        viewModel.dayOfMonth = day
    }

    private fun getViewFromDay(day: Int): TextView? {
        val ordinalDay = day - 1
        return viewBinding?.run {
            when (ordinalDay) {
                MonthDays.DAY_1.ordinal -> dayOfMonth1
                MonthDays.DAY_2.ordinal -> dayOfMonth2
                MonthDays.DAY_3.ordinal -> dayOfMonth3
                MonthDays.DAY_4.ordinal -> dayOfMonth4
                MonthDays.DAY_5.ordinal -> dayOfMonth5
                MonthDays.DAY_6.ordinal -> dayOfMonth6
                MonthDays.DAY_7.ordinal -> dayOfMonth7
                MonthDays.DAY_8.ordinal -> dayOfMonth8
                MonthDays.DAY_9.ordinal -> dayOfMonth9
                MonthDays.DAY_10.ordinal -> dayOfMonth10
                MonthDays.DAY_11.ordinal -> dayOfMonth11
                MonthDays.DAY_12.ordinal -> dayOfMonth12
                MonthDays.DAY_13.ordinal -> dayOfMonth13
                MonthDays.DAY_14.ordinal -> dayOfMonth14
                MonthDays.DAY_15.ordinal -> dayOfMonth15
                MonthDays.DAY_16.ordinal -> dayOfMonth16
                MonthDays.DAY_17.ordinal -> dayOfMonth17
                MonthDays.DAY_18.ordinal -> dayOfMonth18
                MonthDays.DAY_19.ordinal -> dayOfMonth19
                MonthDays.DAY_20.ordinal -> dayOfMonth20
                MonthDays.DAY_21.ordinal -> dayOfMonth21
                MonthDays.DAY_22.ordinal -> dayOfMonth22
                MonthDays.DAY_23.ordinal -> dayOfMonth23
                MonthDays.DAY_24.ordinal -> dayOfMonth24
                MonthDays.DAY_25.ordinal -> dayOfMonth25
                MonthDays.DAY_26.ordinal -> dayOfMonth26
                MonthDays.DAY_27.ordinal -> dayOfMonth27
                MonthDays.DAY_28.ordinal -> dayOfMonth28
                MonthDays.DAY_29.ordinal -> dayOfMonth29
                MonthDays.DAY_30.ordinal -> dayOfMonth30
                MonthDays.DAY_31.ordinal -> dayOfMonth31
                else -> throw IllegalArgumentException("Unknown day of week: $day")
            }
        }
    }
}

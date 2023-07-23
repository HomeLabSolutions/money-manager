package com.d9tilov.android.regular.transaction.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.isEmpty
import com.d9tilov.android.category.ui.BaseCategoryFragment.Companion.ARG_CATEGORY
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.common.android.utils.createTintDrawable
import com.d9tilov.android.common.android.utils.gone
import com.d9tilov.android.common.android.utils.hide
import com.d9tilov.android.common.android.utils.let2
import com.d9tilov.android.common.android.utils.onChange
import com.d9tilov.android.common.android.utils.show
import com.d9tilov.android.common.android.utils.showKeyboard
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.PeriodType
import com.d9tilov.android.core.model.isIncome
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toBigDecimal
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.domain.model.WeekDays
import com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionCreatedNavigator
import com.d9tilov.android.regular.transaction.ui.vm.CreatedRegularTransactionViewModel
import com.d9tilov.android.regular_transaction_ui.R
import com.d9tilov.android.regular_transaction_ui.databinding.FragmentRegularTransactionCreationBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegularTransactionCreationFragment :
    BaseFragment<RegularTransactionCreatedNavigator, FragmentRegularTransactionCreationBinding>(
        FragmentRegularTransactionCreationBinding::inflate,
        R.layout.fragment_regular_transaction_creation
    ),
    RegularTransactionCreatedNavigator {

    private val args by navArgs<RegularTransactionCreationFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val regularTransaction: RegularTransaction? by lazy { args.regularTransaction }
    private val spinnerPeriodMap = arrayMapOf(
        PeriodType.DAY to 0,
        PeriodType.WEEK to 1,
        PeriodType.MONTH to 2
    )
    private var toolbar: MaterialToolbar? = null
    private var localTransaction: RegularTransaction = RegularTransaction.EMPTY

    override fun getNavigator() = this
    override val viewModel by viewModels<CreatedRegularTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.action_add -> {}
                        else -> throw IllegalArgumentException("Unknown menu item with id: ${menuItem.itemId}")
                    }
                    return true
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        viewBinding?.run {
            findNavController().currentBackStackEntry?.savedStateHandle?.run {
                getLiveData<Category>(ARG_CATEGORY)
                    .observe(viewLifecycleOwner) { category ->
                        localTransaction = localTransaction.copy(category = category)
                        remove<Category>(ARG_CATEGORY)
                        updateCategoryIcon()
                        updateSaveButtonState()
                    }
                getLiveData<Int>(ARG_DAY_OF_MONTH)
                    .observe(viewLifecycleOwner) { dayOfMonth ->
                        localTransaction = localTransaction.copy(
                            executionPeriod = ExecutionPeriod.EveryMonth(
                                dayOfMonth,
                                currentDateTime().getStartOfDay()
                            )
                        )
                        createdRegularTransactionRepeatStartsDate.text =
                            getString(
                                R.string.regular_transaction_repeat_period_month_2,
                                dayOfMonth.toString()
                            )
                        remove<Int>(ARG_DAY_OF_MONTH)
                    }
            }
            createdRegularTransactionDescription.onChange { description ->
                localTransaction = localTransaction.copy(description = description)
            }
            createdRegularTransactionNotifyCheckbox.setOnCheckedChangeListener { _, isChecked ->
                localTransaction = localTransaction.copy(pushEnabled = isChecked)
            }
            createdRegularTransactionMainSum.moneyEditText.onChange { sum ->
                localTransaction =
                    localTransaction.copy(sum = if (sum.isEmpty()) BigDecimal.ZERO else sum.toBigDecimal)
                updateSaveButtonState()
            }
            createdRegularTransactionRepeatSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (getSelectedPeriodType(position)) {
                            PeriodType.DAY -> {
                                createdRegularTransactionWeekSelector.gone()
                                createdRegularTransactionRepeatStartsDate.hide()
                                createdRegularTransactionRepeatStartsTitle.hide()
                                localTransaction = localTransaction.copy(
                                    executionPeriod = ExecutionPeriod.EveryDay(currentDate().getStartOfDay())
                                )
                            }

                            PeriodType.WEEK -> {
                                createdRegularTransactionWeekSelector.show()
                                createdRegularTransactionRepeatStartsDate.hide()
                                createdRegularTransactionRepeatStartsTitle.hide()
                                setWeekdaySelected(
                                    when (val period = localTransaction.executionPeriod) {
                                        is ExecutionPeriod.EveryWeek -> period.dayOfWeek
                                        else -> currentDate().dayOfWeek.ordinal
                                    }
                                )
                            }

                            PeriodType.MONTH -> {
                                createdRegularTransactionWeekSelector.gone()
                                createdRegularTransactionRepeatStartsDate.show()
                                createdRegularTransactionRepeatStartsTitle.show()
                                if (localTransaction.executionPeriod !is ExecutionPeriod.EveryMonth) {
                                    localTransaction = localTransaction.copy(
                                        executionPeriod = ExecutionPeriod.EveryMonth(
                                            currentDate().dayOfMonth,
                                            currentDate().getStartOfDay()
                                        )
                                    )
                                }
                                createdRegularTransactionRepeatStartsDate.text =
                                    getString(
                                        R.string.regular_transaction_repeat_period_month_2,
                                        when (val period = localTransaction.executionPeriod) {
                                            is ExecutionPeriod.EveryMonth -> period.dayOfMonth.toString()
                                            else -> currentDate().dayOfMonth.toString()
                                        }
                                    )
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            createdRegularTransactionRepeatStartsDate.setOnClickListener { toDayOfMonthDialog() }
            createdRegularTransactionCategoryLayout.setOnClickListener { toFixedCategoryScreen() }
            createdRegularTransactionSave.setOnClickListener { view ->
                if (view.isSelected) {
                    viewModel.saveOrUpdate(localTransaction)
                } else {
                    shakeError()
                }
            }
            createdRegularTransactionMonday.setOnClickListener { setWeekdaySelected(WeekDays.MONDAY.ordinal) }
            createdRegularTransactionTuesday.setOnClickListener { setWeekdaySelected(WeekDays.TUESDAY.ordinal) }
            createdRegularTransactionWednesday.setOnClickListener { setWeekdaySelected(WeekDays.WEDNESDAY.ordinal) }
            createdRegularTransactionThursday.setOnClickListener { setWeekdaySelected(WeekDays.THURSDAY.ordinal) }
            createdRegularTransactionFriday.setOnClickListener { setWeekdaySelected(WeekDays.FRIDAY.ordinal) }
            createdRegularTransactionSaturday.setOnClickListener { setWeekdaySelected(WeekDays.SATURDAY.ordinal) }
            createdRegularTransactionSunday.setOnClickListener { setWeekdaySelected(WeekDays.SUNDAY.ordinal) }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.defaultTransaction
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { defaultTransaction ->
                    if (localTransaction == RegularTransaction.EMPTY) {
                        localTransaction = regularTransaction ?: defaultTransaction
                    }
                    localTransaction = localTransaction.copy(type = transactionType)
                    initUi()
                }
        }
    }

    private fun toFixedCategoryScreen() {
        val action = RegularTransactionCreationFragmentDirections.toFixedCategoryDest(
            destination = CategoryDestination.EditRegularTransactionScreen,
            transactionType = transactionType
        )
        findNavController().navigate(action)
    }

    private fun toDayOfMonthDialog() {
        val action = RegularTransactionCreationFragmentDirections.toDayOfMonthDest(
            (localTransaction.executionPeriod as ExecutionPeriod.EveryMonth).dayOfMonth
        )
        findNavController().navigate(action)
    }

    private fun initUi() {
        initToolbar()
        updateCategoryIcon()
        updateCurrency()
        updateSaveButtonState()
        viewBinding?.run {
            if (!localTransaction.isValid()) {
                createdRegularTransactionCategoryArrow.show()
                createdRegularTransactionDelete.gone()
            } else {
                createdRegularTransactionCategoryArrow.gone()
                createdRegularTransactionDelete.show()
            }
            createdRegularTransactionMainSum.setValue(
                localTransaction.sum,
                localTransaction.currencyCode
            )
            createdRegularTransactionDescription.setText(localTransaction.description)
            createdRegularTransactionNotifyCheckbox.isChecked = localTransaction.pushEnabled
            createdRegularTransactionRepeatSpinner.setSelection(
                spinnerPeriodMap.getValue(
                    localTransaction.executionPeriod.periodType
                )
            )
        }
    }

    private fun updateCurrency() {
        viewBinding?.createdRegularTransactionMainSum?.setValue(
            localTransaction.sum,
            localTransaction.currencyCode
        )
    }

    private fun shakeError() {
        val animation = AnimationUtils.loadAnimation(
            context,
            R.anim.animation_shake
        )
        viewBinding?.run {
            if (localTransaction.sum.signum() == 0) {
                createdRegularTransactionMainSum.startAnimation(animation)
            } else if (localTransaction.category.isEmpty()) {
                createdRegularTransactionCategoryLayout.startAnimation(animation)
            } else if (localTransaction.executionPeriod.periodType == PeriodType.WEEK && (localTransaction.executionPeriod as ExecutionPeriod.EveryWeek).dayOfWeek == 0) {
                createdRegularTransactionWeekSelector.startAnimation(animation)
            }
        }
    }

    private fun setWeekdaySelected(day: Int) {
        for (i in WeekDays.values()) {
            viewBinding?.run {
                val view = when (i) {
                    WeekDays.MONDAY -> createdRegularTransactionMonday
                    WeekDays.TUESDAY -> createdRegularTransactionTuesday
                    WeekDays.WEDNESDAY -> createdRegularTransactionWednesday
                    WeekDays.THURSDAY -> createdRegularTransactionThursday
                    WeekDays.FRIDAY -> createdRegularTransactionFriday
                    WeekDays.SATURDAY -> createdRegularTransactionSaturday
                    WeekDays.SUNDAY -> createdRegularTransactionSunday
                }
                view.isSelected = i.ordinal == day
            }
        }
        localTransaction =
            localTransaction.copy(
                executionPeriod = ExecutionPeriod.EveryWeek(
                    day,
                    currentDateTime().getStartOfDay()
                )
            )
    }

    private fun updateSaveButtonState() {
        viewBinding?.createdRegularTransactionSave?.isSelected =
            localTransaction.sum.signum() > 0 && !localTransaction.category.isEmpty()
    }

    private fun updateCategoryIcon() {
        viewBinding?.run {
            if (!localTransaction.category.isEmpty()) {
                val iconDrawable = let2(
                    localTransaction.category.icon,
                    localTransaction.category.color
                ) { icon, color ->
                    createTintDrawable(requireContext(), icon, color)
                }

                iconDrawable?.setBounds(LEFT_BOUND, TOP_BOUND, RIGHT_BOUND, BOTTOM_BOUND)
                createdRegularTransactionCategory.setCompoundDrawables(
                    iconDrawable,
                    null,
                    null,
                    null
                )
                createdRegularTransactionCategory.text = localTransaction.category.name
                localTransaction.category.color?.let { color ->
                    createdRegularTransactionCategory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            color
                        )
                    )
                }
            } else {
                createdRegularTransactionCategory.text =
                    getString(R.string.regular_transaction_choose_category)
            }
        }
    }

    private fun getSelectedPeriodType(position: Int): PeriodType {
        return when (position) {
            0 -> PeriodType.DAY
            1 -> PeriodType.WEEK
            2 -> PeriodType.MONTH
            else -> throw IllegalArgumentException("Unknown periodType at position: $position")
        }
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(viewBinding?.createdRegularTransactionMainSum?.moneyEditText)
    }

    private fun initToolbar() {
        toolbar = viewBinding?.createdRegularTransactionToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title =
            getString(
                if (transactionType.isIncome()) {
                    R.string.title_prepopulate_regular_incomes
                } else {
                    R.string.title_prepopulate_regular_expenses
                }
            )
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun back() {
        findNavController().popBackStack()
    }

    companion object {
        const val ARG_DAY_OF_MONTH = "arg_day_of_month"
        private const val LEFT_BOUND = 0
        private const val TOP_BOUND = 0
        private const val RIGHT_BOUND = 120
        private const val BOTTOM_BOUND = 120
    }
}

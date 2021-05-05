package com.d9tilov.moneymanager.periodic.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.FixedCreatedNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment.Companion.ARG_CATEGORY
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.DateValidatorPointBackward
import com.d9tilov.moneymanager.core.util.TRANSACTION_DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.getDayOfWeek
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.core.util.toBigDecimal
import com.d9tilov.moneymanager.databinding.FragmentCreationPeriodicTransactionBinding
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.periodic.vm.CreatedPeriodicTransactionViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class PeriodicTransactionCreationFragment :
    BaseFragment<FixedCreatedNavigator>(R.layout.fragment_creation_periodic_transaction),
    FixedCreatedNavigator {

    private val args by navArgs<PeriodicTransactionCreationFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val fixedTransaction by lazy { args.periodicTransaction }
    private val spinnerPeriodMap = arrayMapOf(
        PeriodType.DAY to 0,
        PeriodType.WEEK to 1,
        PeriodType.MONTH to 2,
        PeriodType.YEAR to 3
    )
    private val viewBinding by viewBinding(FragmentCreationPeriodicTransactionBinding::bind)
    private var toolbar: MaterialToolbar? = null

    override fun getNavigator() = this

    override val viewModel by viewModels<CreatedPeriodicTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        fixedTransaction.let {
            viewModel.run {
                sum.value = sum.value ?: it?.sum ?: BigDecimal.ZERO
                category.value = category.value ?: it?.category
                periodType.value = periodType.value ?: it?.periodType ?: getSelectedPeriodType(
                    viewBinding.createdPeriodicTransactionRepeatSpinner.selectedItemPosition
                )
                startDate.value = startDate.value ?: it?.startDate ?: Date()
                description.value = description.value ?: it?.description ?: ""
                pushEnabled.value = pushEnabled.value ?: it?.pushEnable ?: true
                weekDaysSelected.value =
                    weekDaysSelected.value ?: it?.dayOfWeek ?: Date().getDayOfWeek()
                if (weekDaysSelected.value == 0) {
                    weekDaysSelected.value = Date().getDayOfWeek()
                }
            }
        }
        updateIcon()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(
            ARG_CATEGORY
        )?.observe(
            viewLifecycleOwner
        ) {
            viewModel.category.value = it
            updateIcon()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Category>(
                ARG_CATEGORY
            )
        }
        viewModel.category.observe(this.viewLifecycleOwner, { updateSaveButtonState() })
        viewBinding.run {
            if (fixedTransaction == null) {
                createdPeriodicTransactionCategoryArrow.show()
                createdPeriodicTransactionDelete.gone()
            } else {
                createdPeriodicTransactionCategoryArrow.gone()
                createdPeriodicTransactionDelete.show()
            }
            updateSaveButtonState()
            createdPeriodicTransactionMainSum.setValue(viewModel.sum.value ?: BigDecimal.ZERO)
            createdPeriodicTransactionMainSum.moneyEditText.onChange { sum ->
                if (sum.isEmpty()) {
                    viewModel.sum.value = BigDecimal.ZERO
                } else {
                    viewModel.sum.value = sum.toBigDecimal
                }
                updateSaveButtonState()
            }
            createdPeriodicTransactionDescription.onChange { description ->
                viewModel.description.value = description
            }
            createdPeriodicTransactionRepeatCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.pushEnabled.value = isChecked
            }
            createdPeriodicTransactionRepeatSpinner.setSelection(
                spinnerPeriodMap.getValue(fixedTransaction?.periodType ?: PeriodType.MONTH)
            )
            createdPeriodicTransactionDescription.setText(viewModel.description.value)
            createdPeriodicTransactionRepeatCheckbox.isChecked = viewModel.pushEnabled.value ?: true
            createdPeriodicTransactionRepeatStartsDate.text = SimpleDateFormat(
                TRANSACTION_DATE_FORMAT,
                Locale.getDefault()
            ).format(viewModel.startDate.value ?: Date())
            createdPeriodicTransactionRepeatSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.periodType.value = getSelectedPeriodType(position)
                        if (getSelectedPeriodType(position) == PeriodType.WEEK) {
                            createdPeriodicTransactionWeekSelector.show()
                        } else {
                            createdPeriodicTransactionWeekSelector.gone()
                        }
                        if (getSelectedPeriodType(position) == PeriodType.WEEK) {
                            setAllDays()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            createdPeriodicTransactionRepeatStartsDate.setOnClickListener {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setEnd(Date().time)
                            .setValidator(DateValidatorPointBackward.now())
                            .build()
                    )
                    .setSelection(viewModel.startDate.value?.time ?: Date().time)
                    .build()
                picker.addOnPositiveButtonClickListener { calendarDate ->
                    viewModel.startDate.value = Date(calendarDate)
                    viewBinding.createdPeriodicTransactionRepeatStartsDate.text = SimpleDateFormat(
                        TRANSACTION_DATE_FORMAT,
                        Locale.getDefault()
                    ).format(viewModel.startDate.value ?: Date())
                }
                picker.show(parentFragmentManager, picker.tag)
            }
            createdPeriodicTransactionCategoryLayout.setOnClickListener {
                val action = PeriodicTransactionCreationFragmentDirections.toFixedCategoryDest(
                    destination = CategoryDestination.PREPOPULATE_SCREEN,
                    transactionType = transactionType
                )
                findNavController().navigate(action)
            }
            createdPeriodicTransactionSave.setOnClickListener { view ->
                if (view.isSelected) {
                    viewModel.save()
                } else {
                    shakeError()
                }
            }
            createdPeriodicTransactionSunday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.SUNDAY)
            }
            createdPeriodicTransactionMonday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.MONDAY)
            }
            createdPeriodicTransactionTuesday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.TUESDAY)
            }
            createdPeriodicTransactionWednesday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.WEDNESDAY)
            }
            createdPeriodicTransactionThursday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.THURSDAY)
            }
            createdPeriodicTransactionFriday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.FRIDAY)
            }
            createdPeriodicTransactionSaturday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.SATURDAY)
            }
        }
    }

    private fun shakeError() {
        val animation = AnimationUtils.loadAnimation(
            context,
            R.anim.animation_shake
        )
        if (viewModel.sum.value?.signum() == 0) {
            viewBinding.createdPeriodicTransactionMainSum.startAnimation(animation)
        } else if (viewModel.category.value == null) {
            viewBinding.createdPeriodicTransactionCategoryLayout.startAnimation(animation)
        } else if (viewModel.periodType.value == PeriodType.WEEK && viewModel.weekDaysSelected.value == 0) {
            viewBinding.createdPeriodicTransactionWeekSelector.startAnimation(animation)
        }
    }

    private fun setWeekdaySelected(day: Int, changeSelection: Boolean = true) {
        val byteAsDay = viewModel.weekDaysSelected.value ?: 0b0000000
        val byteToShift = day - 1
        val isSelected = (byteAsDay shr byteToShift) and 1 == 1
        if (changeSelection) {
            viewModel.weekDaysSelected.value = if (isSelected) {
                byteAsDay and (1 shl byteToShift).inv()
            } else {
                byteAsDay or (1 shl byteToShift)
            }
        }
        viewBinding.run {
            val view = when (day) {
                Calendar.SUNDAY -> createdPeriodicTransactionSunday
                Calendar.MONDAY -> createdPeriodicTransactionMonday
                Calendar.TUESDAY -> createdPeriodicTransactionTuesday
                Calendar.WEDNESDAY -> createdPeriodicTransactionWednesday
                Calendar.THURSDAY -> createdPeriodicTransactionThursday
                Calendar.FRIDAY -> createdPeriodicTransactionFriday
                Calendar.SATURDAY -> createdPeriodicTransactionSaturday
                else -> throw IllegalArgumentException("Unknown day of week: $day")
            }
            view.isSelected = if (changeSelection) !isSelected else isSelected
            view.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (if (changeSelection) !isSelected else isSelected) R.color.primary_color else R.color.control_activated_color
                )
            )
        }
        updateSaveButtonState()
    }

    private fun setAllDays() {
        setWeekdaySelected(Calendar.SUNDAY, false)
        setWeekdaySelected(Calendar.MONDAY, false)
        setWeekdaySelected(Calendar.TUESDAY, false)
        setWeekdaySelected(Calendar.WEDNESDAY, false)
        setWeekdaySelected(Calendar.THURSDAY, false)
        setWeekdaySelected(Calendar.FRIDAY, false)
        setWeekdaySelected(Calendar.SATURDAY, false)
    }

    private fun updateSaveButtonState() {
        viewBinding.createdPeriodicTransactionSave.isSelected =
            viewModel.sum.value?.signum() ?: BigDecimal.ZERO.signum() > 0 && viewModel.category.value != null && (viewModel.periodType.value != PeriodType.WEEK || (viewModel.periodType.value == PeriodType.WEEK && viewModel.weekDaysSelected.value != 0))
    }

    private fun updateIcon() {
        viewModel.category.value?.let { category ->
            val iconDrawable = createTintDrawable(
                requireContext(),
                category.icon,
                category.color
            )
            iconDrawable.setBounds(0, 0, 120, 120)
            viewBinding.run {
                createdPeriodicTransactionCategory.setCompoundDrawables(
                    iconDrawable,
                    null,
                    null,
                    null
                )
                createdPeriodicTransactionCategory.text = category.name
                createdPeriodicTransactionCategory.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        category.color
                    )
                )
            }
        }
        if (viewModel.category.value == null) {
            viewBinding.createdPeriodicTransactionCategory.text =
                getString(R.string.fixed_transaction_choose_category)
        }
    }

    private fun getSelectedPeriodType(position: Int): PeriodType {
        return when (position) {
            0 -> PeriodType.DAY
            1 -> PeriodType.WEEK
            2 -> PeriodType.MONTH
            3 -> PeriodType.YEAR
            else -> throw IllegalArgumentException("Unknown periodType at position: $position")
        }
    }

    override fun onStart() {
        super.onStart()
        if (fixedTransaction == null) {
            showKeyboard(viewBinding.createdPeriodicTransactionMainSum.moneyEditText)
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding.createdPeriodicTransactionToolbarContainer.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title =
            getString(
                if (transactionType == TransactionType.INCOME) R.string.title_prepopulate_fixed_income
                else R.string.title_prepopulate_fixed_expense
            )
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                }
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun back() {
        findNavController().popBackStack()
    }
}

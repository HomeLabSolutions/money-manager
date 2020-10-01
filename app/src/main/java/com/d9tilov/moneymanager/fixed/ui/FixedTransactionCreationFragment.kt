package com.d9tilov.moneymanager.fixed.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.d9tilov.moneymanager.core.util.DateValidatorPointBackward
import com.d9tilov.moneymanager.core.util.TRANSACTION_DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.getDayOfWeek
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.core.util.toBigDecimal
import com.d9tilov.moneymanager.databinding.FragmentCreationFixedTransactionBinding
import com.d9tilov.moneymanager.fixed.vm.CreatedFixedTransactionViewModel
import com.d9tilov.moneymanager.period.PeriodType
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
class FixedTransactionCreationFragment :
    BaseFragment<FragmentCreationFixedTransactionBinding, FixedCreatedNavigator>(R.layout.fragment_creation_fixed_transaction),
    FixedCreatedNavigator {

    private val args by navArgs<FixedTransactionCreationFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val fixedTransaction by lazy { args.fixedTransaction }
    private val spinnerPeriodMap = arrayMapOf(
        PeriodType.DAY to 0,
        PeriodType.WEEK to 1,
        PeriodType.MONTH to 2,
        PeriodType.YEAR to 3
    )

    private var toolbar: MaterialToolbar? = null

    override fun performDataBinding(view: View) = FragmentCreationFixedTransactionBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<CreatedFixedTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        fixedTransaction.let {
            viewModel.run {
                sum.value = sum.value ?: it?.sum ?: BigDecimal.ZERO
                category.value = category.value ?: it?.category
                periodType.value = periodType.value ?: it?.periodType
                    ?: getSelectedPeriodType(
                        viewBinding?.createdFixedTransactionRepeatSpinner?.selectedItemPosition ?: 0
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
        viewBinding?.run {
            createdFixedTransactionCategoryArrow.visibility =
                if (fixedTransaction == null) VISIBLE else GONE
            createdFixedTransactionDelete.visibility =
                if (fixedTransaction == null) GONE else VISIBLE
            updateSaveButtonState()
            createdFixedTransactionMainSum.setValue(viewModel.sum.value ?: BigDecimal.ZERO)
            createdFixedTransactionMainSum.moneyEditText.onChange { sum ->
                if (sum.isEmpty()) {
                    viewModel.sum.value = BigDecimal.ZERO
                } else {
                    viewModel.sum.value = sum.toBigDecimal
                }
                updateSaveButtonState()
            }
            createdFixedTransactionDescription.onChange { description ->
                viewModel.description.value = description
            }
            createdFixedTransactionRepeatCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.pushEnabled.value = isChecked
            }
            createdFixedTransactionRepeatSpinner.setSelection(
                spinnerPeriodMap.getValue(fixedTransaction?.let { it.periodType } ?: PeriodType.MONTH)
            )
            createdFixedTransactionDescription.setText(viewModel.description.value)
            createdFixedTransactionRepeatCheckbox.isChecked = viewModel.pushEnabled.value ?: true
            createdFixedTransactionRepeatStartsDate.text = SimpleDateFormat(
                TRANSACTION_DATE_FORMAT,
                Locale.getDefault()
            ).format(viewModel.startDate.value ?: Date())
            createdFixedTransactionRepeatSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.periodType.value = getSelectedPeriodType(position)
                        createdFixedTransactionWeekSelector.visibility =
                            if (getSelectedPeriodType(position) == PeriodType.WEEK) VISIBLE else GONE
                        if (getSelectedPeriodType(position) == PeriodType.WEEK) {
                            setAllDays()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            createdFixedTransactionRepeatStartsDate.setOnClickListener {
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
                    viewBinding?.createdFixedTransactionRepeatStartsDate?.text = SimpleDateFormat(
                        TRANSACTION_DATE_FORMAT,
                        Locale.getDefault()
                    ).format(viewModel.startDate.value ?: Date())
                }
                picker.show(parentFragmentManager, picker.tag)
            }
            createdFixedTransactionCategoryLayout.setOnClickListener {
                val action = FixedTransactionCreationFragmentDirections.toFixedCategoryDest(
                    destination = CategoryDestination.PREPOPULATE_SCREEN,
                    transactionType = transactionType
                )
                findNavController().navigate(action)
            }
            createdFixedTransactionSave.setOnClickListener { view ->
                if (view.isSelected) {
                    viewModel.save()
                } else {
                    shakeError()
                }
            }
            createdFixedTransactionSunday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.SUNDAY)
            }
            createdFixedTransactionMonday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.MONDAY)
            }
            createdFixedTransactionTuesday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.TUESDAY)
            }
            createdFixedTransactionWednesday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.WEDNESDAY)
            }
            createdFixedTransactionThursday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.THURSDAY)
            }
            createdFixedTransactionFriday.setOnClickListener { _ ->
                setWeekdaySelected(Calendar.FRIDAY)
            }
            createdFixedTransactionSaturday.setOnClickListener { _ ->
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
            viewBinding?.createdFixedTransactionMainSum?.startAnimation(animation)
        } else if (viewModel.category.value == null) {
            viewBinding?.createdFixedTransactionCategoryLayout?.startAnimation(animation)
        } else if (viewModel.periodType.value == PeriodType.WEEK && viewModel.weekDaysSelected.value == 0) {
            viewBinding?.createdFixedTransactionWeekSelector?.startAnimation(animation)
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
        viewBinding?.run {
            val view = when (day) {
                Calendar.SUNDAY -> createdFixedTransactionSunday
                Calendar.MONDAY -> createdFixedTransactionMonday
                Calendar.TUESDAY -> createdFixedTransactionTuesday
                Calendar.WEDNESDAY -> createdFixedTransactionWednesday
                Calendar.THURSDAY -> createdFixedTransactionThursday
                Calendar.FRIDAY -> createdFixedTransactionFriday
                Calendar.SATURDAY -> createdFixedTransactionSaturday
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
        viewBinding?.createdFixedTransactionSave?.isSelected =
            viewModel.sum.value?.signum() ?: BigDecimal.ZERO.signum() > 0 &&
            viewModel.category.value != null &&
            (
                viewModel.periodType.value != PeriodType.WEEK ||
                    (viewModel.periodType.value == PeriodType.WEEK && viewModel.weekDaysSelected.value != 0)
                )
    }

    private fun updateIcon() {
        viewModel.category.value?.let { category ->
            val iconDrawable = createTintDrawable(
                requireContext(),
                category.icon,
                category.color
            )
            iconDrawable.setBounds(0, 0, 120, 120)
            viewBinding?.run {
                createdFixedTransactionCategory.setCompoundDrawables(
                    iconDrawable,
                    null,
                    null,
                    null
                )
                createdFixedTransactionCategory.text = category.name
                createdFixedTransactionCategory.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        category.color
                    )
                )
            }
        }
        if (viewModel.category.value == null) {
            viewBinding?.createdFixedTransactionCategory?.text =
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
            showKeyboard(viewBinding?.createdFixedTransactionMainSum?.moneyEditText)
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding?.createdFixedTransactionToolbarContainer?.toolbar
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

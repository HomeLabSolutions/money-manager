package com.d9tilov.moneymanager.regular.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arrayMapOf
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.RegularTransactionCreatedNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment.Companion.ARG_CATEGORY
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.TRANSACTION_DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hide
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.core.util.toBigDecimal
import com.d9tilov.moneymanager.core.util.toLocalDate
import com.d9tilov.moneymanager.core.util.toLocalDateTime
import com.d9tilov.moneymanager.core.util.toMillis
import com.d9tilov.moneymanager.currency.CurrencyDestination
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.ui.CurrencyFragment
import com.d9tilov.moneymanager.databinding.FragmentCreationRegularTransactionBinding
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.regular.vm.CreatedRegularTransactionViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class RegularTransactionCreationFragment :
    BaseFragment<RegularTransactionCreatedNavigator>(R.layout.fragment_creation_regular_transaction),
    RegularTransactionCreatedNavigator {

    private val args by navArgs<RegularTransactionCreationFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val regularTransaction by lazy { args.regularTransaction }
    private val spinnerPeriodMap = arrayMapOf(
        PeriodType.DAY to 0,
        PeriodType.WEEK to 1,
        PeriodType.MONTH to 2
    )
    private val viewBinding by viewBinding(FragmentCreationRegularTransactionBinding::bind)
    private var toolbar: MaterialToolbar? = null
    private var localTransaction: RegularTransaction? = null

    override fun getNavigator() = this
    override val viewModel by viewModels<CreatedRegularTransactionViewModel> {
        CreatedRegularTransactionViewModel.provideFactory(
            createdRegularTransactionViewModelFactory,
            this,
            arguments
        )
    }

    @Inject
    lateinit var createdRegularTransactionViewModelFactory: CreatedRegularTransactionViewModel.CreatedRegularTransactionViewModelFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(
            ARG_CATEGORY
        )?.observe(
            viewLifecycleOwner
        ) { category ->
            localTransaction = localTransaction?.copy(category = category)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Category>(
                ARG_CATEGORY
            )
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DomainCurrency>(
            CurrencyFragment.ARG_CURRENCY
        )?.observe(
            viewLifecycleOwner
        ) {
            localTransaction = localTransaction?.copy(currencyCode = it.code)
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<DomainCurrency>(
                CurrencyFragment.ARG_CURRENCY
            )
        }
        viewBinding.run {
            createdRegularTransactionDescription.onChange { description ->
                localTransaction = localTransaction?.copy(description = description)
            }
            createdRegularTransactionNotifyCheckbox.setOnCheckedChangeListener { _, isChecked ->
                localTransaction = localTransaction?.copy(pushEnabled = isChecked)
            }
            createdRegularTransactionMainSum.moneyEditText.onChange { sum ->
                localTransaction =
                    localTransaction?.copy(sum = if (sum.isEmpty()) BigDecimal.ZERO else sum.toBigDecimal)
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
                        val periodType = getSelectedPeriodType(position)
                        localTransaction =
                            localTransaction?.copy(periodType = periodType)
                        when (periodType) {
                            PeriodType.DAY -> {
                                createdRegularTransactionWeekSelector.gone()
                                createdRegularTransactionRepeatStartsDate.hide()
                                createdRegularTransactionRepeatStartsTitle.hide()
                                localTransaction = localTransaction?.copy(
                                    startDate = currentDate().plus(1, DateTimeUnit.DAY)
                                        .getStartOfDay()
                                )
                            }
                            PeriodType.WEEK -> {
                                createdRegularTransactionWeekSelector.show()
                                createdRegularTransactionRepeatStartsDate.hide()
                                createdRegularTransactionRepeatStartsTitle.hide()
                            }
                            PeriodType.MONTH -> {
                                createdRegularTransactionWeekSelector.gone()
                                createdRegularTransactionRepeatStartsDate.show()
                                createdRegularTransactionRepeatStartsTitle.show()
                            }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            createdRegularTransactionRepeatStartsDate.setOnClickListener {
                val timezone = TimeZone.getDefault()
                val time = localTransaction?.startDate?.toMillis() ?: currentDateTime().toMillis()
                val offsetTime = time + timezone.getOffset(time)
                val offsetDateTime =
                    offsetTime.toLocalDate().plus(1, DateTimeUnit.DAY).getStartOfDay()
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setStart(
                                currentDateTime().date.plus(1, DateTimeUnit.DAY).getStartOfDay()
                                    .toMillis()
                            )
                            .setValidator(DateValidatorPointForward.from(offsetTime))
                            .build()
                    )
                    .setSelection(offsetDateTime.toMillis())
                    .build()
                picker.addOnPositiveButtonClickListener { calendarDate ->
                    localTransaction = localTransaction?.copy(
                        startDate = calendarDate.toLocalDateTime().getStartOfDay()
                    )
                    viewBinding.createdRegularTransactionRepeatStartsDate.text = SimpleDateFormat(
                        TRANSACTION_DATE_FORMAT,
                        Locale.getDefault()
                    ).format(
                        Date(
                            localTransaction?.startDate?.toMillis()
                                ?: Calendar.getInstance().timeInMillis
                        )
                    )
                }
                picker.show(parentFragmentManager, picker.tag)
            }
            createdRegularTransactionCategoryLayout.setOnClickListener {
                val action = RegularTransactionCreationFragmentDirections.toFixedCategoryDest(
                    destination = CategoryDestination.EDIT_TRANSACTION_SCREEN,
                    transactionType = transactionType
                )
                findNavController().navigate(action)
            }
            createdRegularTransactionSave.setOnClickListener { view ->
                if (view.isSelected) {
                    viewModel.saveOrUpdate(localTransaction!!)
                } else {
                    shakeError()
                }
            }
            createdRegularTransactionMonday.setOnClickListener { setWeekdaySelected(0) }
            createdRegularTransactionTuesday.setOnClickListener { setWeekdaySelected(1) }
            createdRegularTransactionWednesday.setOnClickListener { setWeekdaySelected(2) }
            createdRegularTransactionThursday.setOnClickListener { setWeekdaySelected(3) }
            createdRegularTransactionFriday.setOnClickListener { setWeekdaySelected(4) }
            createdRegularTransactionSaturday.setOnClickListener { setWeekdaySelected(5) }
            createdRegularTransactionSunday.setOnClickListener { setWeekdaySelected(6) }
            createdRegularTransactionMainSum.addOnCurrencyClickListener {
                val action =
                    RegularTransactionCreationFragmentDirections.toCurrencyDest(
                        CurrencyDestination.EDIT_REGULAR_TRANSACTION_SCREEN,
                        localTransaction?.currencyCode
                    )
                findNavController().navigate(action)
            }
        }
        viewModel.defaultTransaction.observe(
            viewLifecycleOwner
        ) {
            if (localTransaction == null) localTransaction = regularTransaction ?: it
            localTransaction = localTransaction!!.copy(type = transactionType)
            initUi()
        }
    }

    private fun initUi() {
        if (localTransaction == null) return
        initToolbar()
        updateCategoryIcon()
        updateCurrency()
        updateSaveButtonState()
        viewBinding.run {
            if (!localTransaction!!.isValid()) {
                createdRegularTransactionCategoryArrow.show()
                createdRegularTransactionDelete.gone()
            } else {
                createdRegularTransactionCategoryArrow.gone()
                createdRegularTransactionDelete.show()
            }
            createdRegularTransactionMainSum.setValue(
                localTransaction!!.sum,
                localTransaction!!.currencyCode
            )
            createdRegularTransactionDescription.setText(localTransaction!!.description)
            createdRegularTransactionNotifyCheckbox.isChecked = localTransaction!!.pushEnabled

            createdRegularTransactionRepeatStartsDate.text = SimpleDateFormat(
                TRANSACTION_DATE_FORMAT,
                Locale.getDefault()
            ).format(Date(localTransaction!!.startDate.toMillis()))
            createdRegularTransactionRepeatSpinner.setSelection(
                spinnerPeriodMap.getValue(localTransaction?.periodType ?: PeriodType.MONTH)
            )
            for ((key, value) in spinnerPeriodMap) {
                if (value == createdRegularTransactionRepeatSpinner.selectedItemPosition && key == PeriodType.WEEK) {
                    setWeekdaySelected(localTransaction?.dayOfWeek ?: 0)
                }
            }
        }
    }

    private fun updateCurrency() {
        localTransaction?.let {
            viewBinding.createdRegularTransactionMainSum.setValue(it.sum, it.currencyCode)
        }
    }

    private fun shakeError() {
        val animation = AnimationUtils.loadAnimation(
            context,
            R.anim.animation_shake
        )
        if (localTransaction?.sum?.signum() == 0) {
            viewBinding.createdRegularTransactionMainSum.startAnimation(animation)
        } else if (localTransaction?.category == Category.EMPTY) {
            viewBinding.createdRegularTransactionCategoryLayout.startAnimation(animation)
        } else if (localTransaction?.periodType == PeriodType.WEEK && localTransaction?.dayOfWeek == 0) {
            viewBinding.createdRegularTransactionWeekSelector.startAnimation(animation)
        }
    }

    private fun setWeekdaySelected(day: Int) {
        for (i in 0..6) {
            viewBinding.run {
                val view = when (i) {
                    0 -> createdRegularTransactionMonday
                    1 -> createdRegularTransactionTuesday
                    2 -> createdRegularTransactionWednesday
                    3 -> createdRegularTransactionThursday
                    4 -> createdRegularTransactionFriday
                    5 -> createdRegularTransactionSaturday
                    6 -> createdRegularTransactionSunday
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
        val curDayOfWeek = currentDate().dayOfWeek.ordinal
        val diffDays = if (day < curDayOfWeek) {
            7 - curDayOfWeek + day
        } else {
            var diffDays = day - curDayOfWeek
            if (diffDays == 0) diffDays = 7
            diffDays
        }
        val nextExecDay = currentDate().plus(diffDays, DateTimeUnit.DAY).getStartOfDay()
        localTransaction = localTransaction?.copy(dayOfWeek = day, startDate = nextExecDay)
        updateSaveButtonState()
    }

    private fun updateSaveButtonState() {
        viewBinding.createdRegularTransactionSave.isSelected = localTransaction?.let {
            it.sum.signum() > 0 && it.category != Category.EMPTY
        } ?: false
    }

    private fun updateCategoryIcon() {
        if (localTransaction?.category != Category.EMPTY) {
            localTransaction?.category?.let {
                val iconDrawable = createTintDrawable(requireContext(), it.icon, it.color)
                iconDrawable.setBounds(0, 0, 120, 120)
                viewBinding.run {
                    createdRegularTransactionCategory.setCompoundDrawables(
                        iconDrawable,
                        null,
                        null,
                        null
                    )
                    createdRegularTransactionCategory.text = it.name
                    createdRegularTransactionCategory.setTextColor(
                        ContextCompat.getColor(requireContext(), it.color)
                    )
                }
            }
        } else {
            viewBinding.createdRegularTransactionCategory.text =
                getString(R.string.regular_transaction_choose_category)
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
        showKeyboard(viewBinding.createdRegularTransactionMainSum.moneyEditText)
    }

    private fun initToolbar() {
        toolbar = viewBinding.createdRegularTransactionToolbarContainer.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title =
            getString(
                if (transactionType == TransactionType.INCOME) R.string.title_prepopulate_regular_income
                else R.string.title_prepopulate_regular_expense
            )
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {}
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun back() {
        findNavController().popBackStack()
    }
}

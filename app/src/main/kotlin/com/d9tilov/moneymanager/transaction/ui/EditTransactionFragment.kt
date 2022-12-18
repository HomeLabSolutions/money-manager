package com.d9tilov.moneymanager.transaction.ui

import android.os.Bundle
import android.text.style.TtsSpan.ARG_CURRENCY
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.android.common_android.utils.TRANSACTION_DATE_FORMAT
import com.d9tilov.android.common_android.utils.currentDateTime
import com.d9tilov.android.common_android.utils.toLocalDateTime
import com.d9tilov.android.common_android.utils.toMillis
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment.Companion.ARG_CATEGORY
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentEditTransactionBinding
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.vm.EditTransactionViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditTransactionFragment : EditTransactionNavigator,
    BaseFragment<EditTransactionNavigator, FragmentEditTransactionBinding>(
        FragmentEditTransactionBinding::inflate,
        R.layout.fragment_edit_transaction
    ) {

    private val args by navArgs<EditTransactionFragmentArgs>()
    private val transaction by lazy { args.editedTransaction }
    private val transactionType by lazy { args.transactionType }

    private var toolbar: MaterialToolbar? = null
    private var localTransaction: Transaction? = null

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun getNavigator() = this

    override val viewModel by viewModels<EditTransactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localTransaction = transaction
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateIcon()
        updateInStatistics()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(
            ARG_CATEGORY
        )?.observe(
            viewLifecycleOwner
        ) {
            localTransaction = localTransaction?.copy(category = it)
            updateIcon()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Category>(
                ARG_CATEGORY
            )
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DomainCurrency>(
            ARG_CURRENCY
        )?.observe(
            viewLifecycleOwner
        ) {
            localTransaction = localTransaction?.copy(currencyCode = it.code)
            updateCurrency()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<DomainCurrency>(
                ARG_CURRENCY
            )
        }
        viewBinding?.run {
            editTransactionSave.setOnClickListener {
                viewModel.update(
                    localTransaction?.copy(
                        sum = editTransactionMainSum.getValue(),
                        date = localTransaction?.date ?: currentDateTime(),
                        description = editTransactionDescription.text.toString()
                    ) ?: transaction
                )
            }
            editTransactionDate.setOnClickListener {
                val timezone = TimeZone.getDefault()
                val time = transaction.date.toMillis()
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setEnd(currentDateTime().toMillis())
                            .setValidator(DateValidatorPointBackward.now())
                            .build()
                    )
                    .setSelection(time + timezone.getOffset(time))
                    .build()
                picker.addOnPositiveButtonClickListener { calendarDate ->
                    val date = calendarDate.toLocalDateTime()
                    localTransaction = localTransaction?.copy(date = date)
                    editTransactionDate.text = SimpleDateFormat(
                        TRANSACTION_DATE_FORMAT,
                        Locale.getDefault()
                    ).format(Date(calendarDate.toLocalDateTime().toMillis()))
                }
                picker.show(parentFragmentManager, picker.tag)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "open_calendar")
                }
            }
            editTransactionCategory.setOnClickListener {
                val action =
                    EditTransactionFragmentDirections.toCategoryDest(
                        CategoryDestination.EditTransactionScreen,
                        transactionType
                    )
                findNavController().navigate(action)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "open_category_screen_of_transaction")
                }
            }
            editTransactionDelete.setOnClickListener {
                val action =
                    EditTransactionFragmentDirections.toRemoveTransactionDialog(transaction)
                findNavController().navigate(action)
            }
            editTransactionDescription.setText(transaction.description)
            editTransactionMainSum.setValue(transaction.sum, transaction.currencyCode)
            editTransactionDate.text = SimpleDateFormat(
                TRANSACTION_DATE_FORMAT,
                Locale.getDefault()
            ).format(Date(transaction.date.toMillis()))
            editTransactionInStatisticsCheckbox.setOnCheckedChangeListener { _, isChecked ->
                localTransaction = localTransaction?.copy(inStatistics = isChecked)
            }
        }
        toolbar = viewBinding?.editTransactionToolbarContainer?.toolbar
        initToolbar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(viewBinding?.editTransactionMainSum?.moneyEditText)
    }

    private fun updateInStatistics() {
        viewBinding?.editTransactionInStatisticsCheckbox?.isChecked = localTransaction!!.inStatistics
    }

    private fun updateIcon() {
        val iconDrawable = createTintDrawable(
            requireContext(),
            localTransaction?.category?.icon ?: transaction.category.icon,
            localTransaction?.category?.color ?: transaction.category.icon
        )
        iconDrawable.setBounds(LEFT_BOUND, TOP_BOUND, RIGHT_BOUND, BOTTOM_BOUND)
        viewBinding?.run {
            editTransactionCategory.setCompoundDrawables(iconDrawable, null, null, null)
            editTransactionCategory.text = localTransaction?.category?.name
            editTransactionCategory.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    localTransaction?.category?.color ?: transaction.category.color
                )
            )
        }
    }

    private fun updateCurrency() {
        localTransaction?.let {
            viewBinding?.editTransactionMainSum?.setValue(it.sum, it.currencyCode)
        }
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_transaction)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun save() {
        findNavController().popBackStack()
    }

    companion object {
        private const val LEFT_BOUND = 0
        private const val TOP_BOUND = 0
        private const val RIGHT_BOUND = 120
        private const val BOTTOM_BOUND = 120
    }
}

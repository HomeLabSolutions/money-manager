package com.d9tilov.android.transaction.ui

import android.os.Bundle
import android.text.style.TtsSpan.ARG_CURRENCY
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.BaseCategoryFragment.Companion.ARG_CATEGORY
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.common.android.utils.TRANSACTION_DATE_FORMAT
import com.d9tilov.android.common.android.utils.createTintDrawable
import com.d9tilov.android.common.android.utils.showKeyboard
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.android.transaction.ui.navigation.EditTransactionNavigator
import com.d9tilov.android.transaction.ui.vm.TransactionCreationViewModel
import com.d9tilov.android.transaction_ui.R
import com.d9tilov.android.transaction_ui.databinding.FragmentEditTransactionBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class EditTransactionFragment : EditTransactionNavigator,
    BaseFragment<EditTransactionNavigator, FragmentEditTransactionBinding>(
        FragmentEditTransactionBinding::inflate,
        R.layout.fragment_edit_transaction
    ) {

    private var toolbar: MaterialToolbar? = null

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun getNavigator() = this

    override val viewModel by viewModels<TransactionCreationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateIcon()
        updateInStatistics()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(
            ARG_CATEGORY
        )?.observe(
            viewLifecycleOwner
        ) {
            viewModel.update(viewModel.editedTransaction.value.copy(category = it))
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
            viewModel.update(viewModel.editedTransaction.value.copy(currencyCode = it.code))
            updateCurrency()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<DomainCurrency>(
                ARG_CURRENCY
            )
        }
        viewBinding?.run {
            editTransactionSave.setOnClickListener {
                viewModel.update(
                    viewModel.editedTransaction.value.copy(
                        sum = editTransactionMainSum.getValue(),
                        description = editTransactionDescription.text.toString()
                    )
                )
            }
            editTransactionDate.setOnClickListener {
                val timezone = TimeZone.getDefault()
                val time = viewModel.editedTransaction.value.date.toMillis()
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
                    viewModel.update(viewModel.editedTransaction.value.copy(date = date))
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
                        viewModel.transactionType.value
                    )
                findNavController().navigate(action)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "open_category_screen_of_transaction")
                }
            }
            editTransactionDelete.setOnClickListener {
                val action =
                    EditTransactionFragmentDirections.toRemoveTransactionDialog(viewModel.editedTransaction.value.id)
                findNavController().navigate(action)
            }
            editTransactionDescription.setText(viewModel.editedTransaction.value.description)
            editTransactionMainSum.setValue(
                viewModel.editedTransaction.value.sum,
                viewModel.editedTransaction.value.currencyCode
            )
            editTransactionDate.text = SimpleDateFormat(
                TRANSACTION_DATE_FORMAT,
                Locale.getDefault()
            ).format(Date(viewModel.editedTransaction.value.date.toMillis()))
            editTransactionInStatisticsCheckbox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.update(viewModel.editedTransaction.value.copy(inStatistics = isChecked))
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
        viewBinding?.editTransactionInStatisticsCheckbox?.isChecked =
            viewModel.editedTransaction.value.inStatistics
    }

    private fun updateIcon() {
        val iconDrawable = createTintDrawable(
            requireContext(),
            viewModel.editedTransaction.value.category.icon,
            viewModel.editedTransaction.value.category.color
        )
        iconDrawable.setBounds(LEFT_BOUND, TOP_BOUND, RIGHT_BOUND, BOTTOM_BOUND)
        viewBinding?.run {
            editTransactionCategory.setCompoundDrawables(iconDrawable, null, null, null)
            editTransactionCategory.text = viewModel.editedTransaction.value.category.name
            val color = viewModel.editedTransaction.value.category.color
            color.let { c ->
                editTransactionCategory.setTextColor(ContextCompat.getColor(requireContext(), c))
            }
        }
    }

    private fun updateCurrency() {
        viewBinding?.editTransactionMainSum?.setValue(
            viewModel.editedTransaction.value.sum,
            viewModel.editedTransaction.value.currencyCode
        )
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

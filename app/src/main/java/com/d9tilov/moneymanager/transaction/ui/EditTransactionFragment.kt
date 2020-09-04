package com.d9tilov.moneymanager.transaction.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment.Companion.ARG_CATEGORY
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.util.DateValidatorPointBackward
import com.d9tilov.moneymanager.core.util.TRANSACTION_DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.databinding.FragmentEditTransactionBinding
import com.d9tilov.moneymanager.transaction.ui.vm.EditTransactionViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class EditTransactionFragment : EditTransactionNavigator,
    BaseFragment<FragmentEditTransactionBinding, EditTransactionNavigator>(
        R.layout.fragment_edit_transaction
    ) {

    private val args by navArgs<EditTransactionFragmentArgs>()
    private val transaction by lazy { args.editedTransaction }
    private val transactionType by lazy { args.transactionType }

    private var toolbar: MaterialToolbar? = null
    private lateinit var date: Date
    private lateinit var category: Category
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun performDataBinding(view: View) = FragmentEditTransactionBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<EditTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date = transaction.date
        category = transaction.category
        updateIcon()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Category>(ARG_CATEGORY)?.observe(
            viewLifecycleOwner
        ) {
            category = it
            updateIcon()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Category>(
                ARG_CATEGORY
            )
        }
        viewBinding?.let {
            it.editTransactionSave.setOnClickListener { _ ->
                viewModel.update(
                    transaction.copy(
                        sum = it.editTransactionMainSum.getValue(),
                        category = category,
                        date = this.date,
                        description = it.editTransactionDescription.text.toString()
                    )
                )
            }
            it.editTransactionDate.setOnClickListener {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setEnd(Date().time)
                            .setValidator(DateValidatorPointBackward.now())
                            .build()
                    )
                    .setSelection(transaction.date.time)
                    .build()
                picker.addOnPositiveButtonClickListener { calendarDate ->
                    this.date = Date(calendarDate)
                    viewBinding?.editTransactionDate?.text = SimpleDateFormat(
                        TRANSACTION_DATE_FORMAT,
                        Locale.getDefault()
                    ).format(date)
                }
                picker.show(parentFragmentManager, picker.tag)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "open_calendar")
                }
            }
            it.editTransactionCategory.setOnClickListener {
                val action =
                    EditTransactionFragmentDirections.toCategoryDest(
                        CategoryDestination.EDIT_TRANSACTION_SCREEN,
                        transactionType
                    )
                findNavController().navigate(action)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.ITEM_ID, "open_category_screen_of_transaction")
                }
            }
            it.editTransactionDelete.setOnClickListener {
                val action = EditTransactionFragmentDirections.toRemoveTransactionDialog(
                    transaction
                )
                findNavController().navigate(action)
            }
            it.editTransactionDescription.setText(transaction.description)
            it.editTransactionMainSum.setValue(transaction.sum)
            it.editTransactionDate.text = SimpleDateFormat(
                TRANSACTION_DATE_FORMAT,
                Locale.getDefault()
            ).format(transaction.date)
        }
        toolbar = viewBinding?.editTransactionToolbarContainer?.toolbar
        initToolbar(toolbar)
    }

    private fun updateIcon() {
        val iconDrawable = createTintDrawable(
            requireContext(),
            category.icon,
            category.color
        )
        iconDrawable.setBounds(0, 0, 120, 120)
        viewBinding?.let {
            it.editTransactionCategory.setCompoundDrawables(iconDrawable, null, null, null)
            it.editTransactionCategory.text = category.name
            it.editTransactionCategory.setTextColor(category.color)
        }
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_transaction)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun save() {
        findNavController().popBackStack()
    }
}

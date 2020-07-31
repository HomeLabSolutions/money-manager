package com.d9tilov.moneymanager.transaction.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.core.util.DateValidatorPointBackward
import com.d9tilov.moneymanager.core.util.TRANSACTION_DATE_FORMAT
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.databinding.FragmentEditTransactionBinding
import com.d9tilov.moneymanager.transaction.ui.vm.EditTransactionViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditTransactionFragment : EditTransactionNavigator,
    BaseFragment<FragmentEditTransactionBinding, EditTransactionNavigator>(
        R.layout.fragment_edit_transaction
    ) {

    private val args by navArgs<EditTransactionFragmentArgs>()
    private val transaction by lazy { args.editedTransaction }
    private val category by lazy { args.category }
    private var toolbar: MaterialToolbar? = null
    private lateinit var date: Date

    override fun performDataBinding(view: View) = FragmentEditTransactionBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<EditTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date = transaction.date
        viewBinding?.let {
            it.editTransactionSave.setOnClickListener { _ ->
                viewModel.update(
                    transaction.copy(
                        sum = it.editTransactionMainSum.getValue(),
                        category = category,
                        date = this.date
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
                picker.addOnPositiveButtonClickListener {
                    this.date = Date(it)
                    viewBinding?.editTransactionDate?.text = SimpleDateFormat(
                        TRANSACTION_DATE_FORMAT,
                        Locale.getDefault()
                    ).format(date)
                }
                picker.show(parentFragmentManager, picker.tag)
            }
            it.editTransactionCategory.setOnClickListener {
                val action =
                    EditTransactionFragmentDirections.toCategoryDest(
                        transaction,
                        CategoryDestination.EDIT_TRANSACTION_SCREEN
                    )
                findNavController().navigate(action)
            }
            it.editTransactionMainSum.setValue(transaction.sum)
            it.editTransactionDate.text = SimpleDateFormat(
                TRANSACTION_DATE_FORMAT,
                Locale.getDefault()
            ).format(transaction.date)
            val icon = createTintDrawable(
                requireContext(),
                category.icon,
                category.color
            )
            icon.setBounds(0, 0, 120, 120)
            it.editTransactionCategory.setCompoundDrawables(icon, null, null, null)
            it.editTransactionCategory.text = category.name
        }
        toolbar = viewBinding?.editTransactionToolbarContainer?.toolbar
        initToolbar(toolbar)
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = getString(R.string.title_transaction)
    }

    override fun save() {
        findNavController().popBackStack()
    }
}

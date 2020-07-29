package com.d9tilov.moneymanager.transaction.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.databinding.FragmentEditTransactionBinding
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.vm.EditTransactionViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTransactionFragment : EditTransactionNavigator,
    BaseFragment<FragmentEditTransactionBinding, EditTransactionNavigator>(
        R.layout.fragment_edit_transaction
    ) {

    private lateinit var transaction: Transaction
    private var toolbar: MaterialToolbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Transaction>(ARG_EDIT_TRANSACTION)?.let {
            transaction = it
        }
    }

    override fun performDataBinding(view: View) = FragmentEditTransactionBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<EditTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            it.editTransactionMainSum.setValue(transaction.sum)
            it.editTransactionCategory.text = transaction.category.name
            val icon = createTintDrawable(
                requireContext(),
                transaction.category.icon,
                transaction.category.color
            )
            icon.setBounds(0, 0, 120, 120)
            it.editTransactionCategory.setCompoundDrawables(icon, null, null, null)
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
        TODO("Not yet implemented")
    }

    override fun back() {
        TODO("Not yet implemented")
    }

    override fun openCategories() {
        TODO("Not yet implemented")
    }

    override fun openCalendar() {
        TODO("Not yet implemented")
    }

    companion object {
        const val ARG_EDIT_TRANSACTION = "args_edit_transaction"
    }
}

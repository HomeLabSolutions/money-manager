package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.RemoveCategoryDialogNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.ui.vm.CategoryRemoveViewModel
import com.d9tilov.moneymanager.databinding.FragmentDialogRemoveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryRemoveDialog :
    BaseDialogFragment<FragmentDialogRemoveBinding, RemoveCategoryDialogNavigator>(),
    RemoveCategoryDialogNavigator {

    private val args by navArgs<CategoryRemoveDialogArgs>()
    private val category by lazy { args.category }
    private val destination by lazy { args.destination }
    private val transactionType by lazy { args.transactionType }

    override val layoutId = R.layout.fragment_dialog_remove
    override fun performDataBinding(view: View) = FragmentDialogRemoveBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryRemoveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            it.transactionRemoveDialogTitle.text = getString(R.string.category_delete_title)
            it.transactionRemoveDialogSubtitle.text =
                getString(
                    if (category.children.isEmpty()) R.string.category_delete_sub_title else R.string.category_delete_group_sub_title,
                    category.name
                )
            it.transactionRemoveButtonConfirm.setOnClickListener { viewModel.remove(category) }
            it.transactionRemoveButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun closeDialog() {
        dismiss()
        if (destination == CategoryDestination.CATEGORY_CREATION_SCREEN) {
            val action = CategoryRemoveDialogDirections.toCategoryDest(
                destination = destination,
                transactionType = transactionType
            )
            findNavController().navigate(action)
        }
    }
}

package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.RemoveSubCategoryDialogNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.ui.vm.SubcategoryRemoveViewModel
import com.d9tilov.moneymanager.databinding.FragmentDialogTripleRemoveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubcategoryRemoveDialogFragment :
    BaseDialogFragment<FragmentDialogTripleRemoveBinding, RemoveSubCategoryDialogNavigator>(),
    RemoveSubCategoryDialogNavigator {

    private val args by navArgs<SubcategoryRemoveDialogFragmentArgs>()
    private val subCategory by lazy { args.subcategory }
    private val destination by lazy { args.destination }
    private val transactionType by lazy { args.transactionType }

    override val layoutId = R.layout.fragment_dialog_triple_remove
    override fun performDataBinding(view: View) = FragmentDialogTripleRemoveBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<SubcategoryRemoveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            it.tripleRemoveDialogButtonAction1.text = getString(R.string.sub_category_delete)
            it.tripleRemoveDialogButtonAction2.text =
                getString(R.string.sub_category_delete_from_group)
            it.tripleRemoveDialogButtonCancel.text = getString(R.string.cancel)
            it.tripleRemoveDialogTitle.text = getString(R.string.sub_category_delete_title)
            it.tripleRemoveDialogSubtitle.text =
                getString(R.string.sub_category_delete_subtitle, subCategory.name)
            it.tripleRemoveDialogButtonAction1.setOnClickListener { viewModel.remove(subCategory) }
            it.tripleRemoveDialogButtonAction2.setOnClickListener {
                viewModel.removeFromGroup(
                    subCategory
                )
            }
            it.tripleRemoveDialogButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun closeDialog() {
        dismiss()
        if (destination == CategoryDestination.SUB_CATEGORY_SCREEN) {
            val action = SubcategoryRemoveDialogFragmentDirections.toSubCategoryDest(
                destination = destination,
                parentCategory = subCategory.parent!!,
                transactionType = transactionType
            )
            findNavController().navigate(action)
        }
    }

    override fun closeDialogAndGoToCategory() {
        val action = SubcategoryRemoveDialogFragmentDirections.toCategoryDest(
            destination = destination,
            transactionType = transactionType
        )
        findNavController().navigate(action)
    }
}
package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategoryUnionDialogNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.exception.CategoryExistException
import com.d9tilov.moneymanager.category.ui.vm.CategoryUnionViewModel
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.databinding.FragmentDialogCategoryUnionBinding
import com.mfms.common.util.hideKeyboard
import com.mfms.common.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryUnitDialog :
    BaseDialogFragment<FragmentDialogCategoryUnionBinding, CategoryUnionDialogNavigator>(),
    CategoryUnionDialogNavigator {

    private val args by navArgs<CategoryUnitDialogArgs>()
    private val firstCategory by lazy { args.firstCategory }
    private val secondCategory by lazy { args.secondCategory }
    private val transactionType by lazy { args.transactionType }

    override val layoutId = R.layout.fragment_dialog_category_union

    override fun performDataBinding(view: View) = FragmentDialogCategoryUnionBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryUnionViewModel>()

    override fun accept() {
        dismiss()
    }

    override fun showError(error: Throwable) {
        if (error is CategoryExistException) {
            viewBinding?.categoryDialogUnionError?.text =
                getString(R.string.category_unit_name_exist_error)
        }
        viewBinding?.categoryDialogUnionError?.visibility = VISIBLE
    }

    override fun cancel() {
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            it.categoryDialogUnionLimit.text = getString(
                R.string.category_unit_to_group_limit,
                it.categoryDialogUnionEtName.text.length.toString(),
                resources.getInteger(R.integer.max_category_name_length).toString()
            )
            it.categoryDialogUnionCancel.setOnClickListener { viewModel.cancel() }
            val firstDrawable =
                createTintDrawable(requireContext(), firstCategory.icon, firstCategory.color)
            it.categoryDialogUnionFolder.categoryDialogUnionItem1.setImageDrawable(firstDrawable)
            if (secondCategory.children.isNotEmpty()) {
                it.categoryDialogUnionTitle.text = getString(R.string.category_add_to_group_title)
                it.categoryDialogUnionSubtitle.text = getString(
                    R.string.category_unit_to_group_subtitle,
                    firstCategory.name,
                    secondCategory.name
                )
                it.categoryDialogUnionEtName.visibility = GONE
                it.categoryDialogUnionLimit.visibility = GONE
                it.categoryDialogUnionError.visibility = GONE
                it.categoryDialogUnionConfirm.text = getString(R.string.add)
                it.categoryDialogCurrentFolder.categoryDialogUnionItem1.setImageDrawable(
                    firstDrawable
                )
                it.categoryDialogCurrentFolder.root.visibility = VISIBLE
            } else {
                it.categoryDialogUnionConfirm.isEnabled =
                    it.categoryDialogUnionEtName.text.isNotEmpty()
                it.categoryDialogUnionSubtitle.text =
                    getString(R.string.category_unit_new_name_subtitle)
                it.categoryDialogUnionTitle.text = getString(R.string.category_unit_to_group_title)
                it.categoryDialogUnionFolder.categoryDialogUnionItem1.setImageDrawable(firstDrawable)
                val secondDrawable =
                    createTintDrawable(requireContext(), secondCategory.icon, secondCategory.color)
                it.categoryDialogUnionFolder.categoryDialogUnionItem2.setImageDrawable(
                    secondDrawable
                )
                it.categoryDialogUnionConfirm.text = getString(R.string.save)
                it.categoryDialogUnionFolder.root.visibility = VISIBLE
            }
            it.categoryDialogUnionConfirm.setOnClickListener {
                if (viewBinding?.categoryDialogUnionEtName?.text.toString().isEmpty()) {
                    showError(IllegalArgumentException())
                } else {
                    viewModel.createGroup(
                        firstCategory,
                        secondCategory,
                        createUnionCategory()
                    )
                }
            }
            it.categoryDialogUnionEtName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val isNameEmpty = s?.length == 0
                    viewBinding?.categoryDialogUnionConfirm?.isEnabled = !isNameEmpty
                    viewBinding?.categoryDialogUnionError?.visibility = INVISIBLE
                    viewBinding?.categoryDialogUnionLimit?.text = getString(
                        R.string.category_unit_to_group_limit,
                        s?.length.toString(),
                        resources.getInteger(R.integer.max_category_name_length).toString()
                    )
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    private fun createUnionCategory(): Category {
        return Category(
            type = transactionType,
            name = viewBinding?.categoryDialogUnionEtName?.text.toString(),
            color = R.color.category_all_color,
            icon = R.drawable.ic_category_folder
        )
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(viewBinding?.categoryDialogUnionEtName)
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }
}

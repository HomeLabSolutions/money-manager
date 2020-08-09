package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategoryCreationNavigator
import com.d9tilov.moneymanager.category.ui.vm.CategoryCreationViewModel
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.databinding.FragmentCreationCategoryBinding
import com.google.android.material.appbar.MaterialToolbar
import com.mfms.common.util.hideKeyboard
import com.mfms.common.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryCreationFragment :
    BaseFragment<FragmentCreationCategoryBinding, CategoryCreationNavigator>(
        R.layout.fragment_creation_category
    ),
    CategoryCreationNavigator {

    private val args by navArgs<CategoryCreationFragmentArgs>()
    private val category by lazy { args.category }
    private var toolbar: MaterialToolbar? = null

    override fun performDataBinding(view: View): FragmentCreationCategoryBinding =
        FragmentCreationCategoryBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryCreationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            it.categoryCreationEtName.setText(category?.name)
            val tintIcon = createTintDrawable(
                requireContext(),
                category?.icon ?: R.drawable.ic_category_food,
                category?.color ?: R.color.category_deep_purple_a100
            )
            it.categoryCreationIcon.setImageDrawable(tintIcon)
            it.categoryCreationNameLimit.text = getString(
                R.string.letter_limit,
                it.categoryCreationEtName.length().toString(),
                resources.getInteger(R.integer.max_category_name_length).toString()
            )
            it.categoryCreationSave.isEnabled =
                it.categoryCreationEtName.length().toString().isNotEmpty()
            it.categoryCreationEtName.onChange { text ->
                it.categoryCreationNameLimit.text = getString(
                    R.string.letter_limit,
                    text.length.toString(),
                    resources.getInteger(R.integer.max_category_name_length).toString()
                )
                it.categoryCreationSave.isEnabled = text.isNotEmpty()
            }
            it.categoryCreationIconLayout.setOnClickListener {
                val action = CategoryCreationFragmentDirections.toCategorySetDest(category)
                findNavController().navigate(action)
            }
        }
        toolbar = viewBinding?.categoryCreationToolbarContainer?.toolbar
        initToolbar(toolbar)
    }

    override fun onStart() {
        showKeyboard(viewBinding?.categoryCreationEtName)
        super.onStart()
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title =
            getString(if (category == null) R.string.title_category_creation else R.string.title_category_creation_create)
    }

    override fun openColorPicker() {
        TODO("Not yet implemented")
    }

    override fun openCategoryIconSet() {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }
}

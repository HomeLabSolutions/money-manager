package com.d9tilov.moneymanager.category.ui

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CategoryCreationNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.recycler.CategoryColorAdapter
import com.d9tilov.moneymanager.category.ui.vm.CategoryCreationViewModel
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.events.OnItemClickListener
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
    private val category by lazy {
        args.category ?: Category(
            type = transactionType,
            name = "",
            icon = R.drawable.ic_category_food,
            color = R.color.category_pink
        )
    }
    private val transactionType by lazy { args.transactionType }
    private val destination by lazy { args.destination }

    @ColorRes
    private var color: Int = 0

    private var toolbar: MaterialToolbar? = null
    private lateinit var categoryColorAdapter: CategoryColorAdapter

    override fun performDataBinding(view: View): FragmentCreationCategoryBinding =
        FragmentCreationCategoryBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryCreationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryColorAdapter = CategoryColorAdapter(category.color)
        categoryColorAdapter.itemClickListener = onItemColorClickListener
    }

    private val onItemColorClickListener = object : OnItemClickListener<Int> {
        override fun onItemClick(item: Int, position: Int) {
            color = item
            viewBinding?.let {
                it.categoryCreationRvColorPicker.visibility = INVISIBLE
                setColorToIcon()
                setColorToColorIcon()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color = category.color
        viewBinding?.let {
            val colorLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.categoryCreationRvColorPicker.layoutManager = colorLayoutManager
            it.categoryCreationRvColorPicker.adapter = categoryColorAdapter
            it.categoryCreationRvColorPicker.scrollToPosition(categoryColorAdapter.getSelectedPosition())
            it.categoryCreationEtName.setText(category.name)
            setColorToIcon()
            setColorToColorIcon()
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
            it.categoryCreationDelete.visibility =
                if (category.id == DEFAULT_DATA_ID) GONE else VISIBLE
            it.categoryCreationIconLayout.setOnClickListener { _ ->
                val action = CategoryCreationFragmentDirections.toCategorySetDest(
                    category.copy(
                        name = it.categoryCreationEtName.text.toString(),
                        color = color
                    ),
                    transactionType,
                    destination
                )
                findNavController().navigate(action)
            }
            it.categoryCreationSave.setOnClickListener { _ ->
                viewModel.save(
                    category.copy(
                        name = it.categoryCreationEtName.text.toString(),
                        color = color,
                        icon = category.icon
                    )
                )
            }
            it.categoryCreationColor.setOnClickListener { _ ->
                it.categoryCreationRvColorPicker.visibility = VISIBLE
            }
            it.categoryCreationDelete.setOnClickListener {
                if (category.id == DEFAULT_DATA_ID) {
                    return@setOnClickListener
                }
                val action = if (category.parent != null) {
                    CategoryCreationFragmentDirections.toRemoveSubCategoryDialog(
                        destination,
                        transactionType,
                        category
                    )
                } else {
                    CategoryCreationFragmentDirections.toRemoveCategoryDialog(category, CategoryDestination.CATEGORY_CREATION_SCREEN, transactionType)
                }
                findNavController().navigate(action)
            }
        }
        toolbar = viewBinding?.categoryCreationToolbarContainer?.toolbar
        initToolbar(toolbar)
    }

    override fun onStart() {
        if (category.id == DEFAULT_DATA_ID) {
            showKeyboard(viewBinding?.categoryCreationEtName)
        }
        super.onStart()
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    private fun setColorToColorIcon() {
        val colorDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_rectange_round_corners
        ) as GradientDrawable
        colorDrawable.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), color),
            PorterDuff.Mode.SRC_ATOP
        )
        viewBinding?.categoryCreationColor?.background = colorDrawable
    }

    private fun setColorToIcon() {
        val tintIcon = createTintDrawable(
            requireContext(),
            category.icon,
            color
        )
        viewBinding?.categoryCreationIcon?.setImageDrawable(tintIcon)
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title =
            getString(if (category.id != DataConstants.DEFAULT_DATA_ID) R.string.title_category_creation else R.string.title_category_creation_create)
    }

    override fun save() {
        val action = if (destination == CategoryDestination.SUB_CATEGORY_SCREEN) {
            CategoryCreationFragmentDirections.toSubCategoryDest(
                transactionType = transactionType,
                destination = destination,
                parentCategory = category.parent!!
            )
        } else {
            CategoryCreationFragmentDirections.toCategoryDest(
                transactionType = transactionType,
                destination = destination
            )
        }
        findNavController().navigate(action)
    }
}

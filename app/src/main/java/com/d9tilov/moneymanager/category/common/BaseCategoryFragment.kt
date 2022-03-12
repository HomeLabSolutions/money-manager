package com.d9tilov.moneymanager.category.common

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.ui.CategoryFragmentArgs
import com.d9tilov.moneymanager.category.ui.recycler.CategoryModifyAdapter
import com.d9tilov.moneymanager.core.events.OnBackPressed
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemLongClickListener
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hide
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentCategoryBinding
import com.d9tilov.moneymanager.databinding.LayoutEmptyListPlaceholderBinding
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseCategoryFragment<N : BaseNavigator> :
    BaseFragment<N>(R.layout.fragment_category),
    OnBackPressed {

    private val args by navArgs<CategoryFragmentArgs>()
    protected val destination by lazy { args.destination }
    protected val transactionType by lazy { args.transactionType }

    protected val viewBinding by viewBinding(FragmentCategoryBinding::bind)
    protected var toolbar: MaterialToolbar? = null
    protected lateinit var categoryAdapter: CategoryModifyAdapter
    private var viewStub: LayoutEmptyListPlaceholderBinding? = null

    private val onItemClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            (viewModel as BaseCategoryViewModel<N>).onCategoryClicked(item)
        }
    }

    private val onItemLongClickListener = object : OnItemLongClickListener<Category> {
        override fun onItemLongClick(item: Category, position: Int) {
            categoryAdapter.enableEditMode(true)
        }
    }

    private val onItemRemoveClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            (viewModel as BaseCategoryViewModel<N>).onCategoryRemoved(item)
            categoryAdapter.enableEditMode(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryModifyAdapter()
        categoryAdapter.itemClickListener = onItemClickListener
        categoryAdapter.itemLongClickListener = onItemLongClickListener
        categoryAdapter.itemRemoveClickListener = onItemRemoveClickListener
    }

    override fun onStop() {
        categoryAdapter.enableEditMode(false)
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStub = viewBinding.categoryEmptyPlaceholder
        initToolbar()
        viewBinding.run {
            val layoutManager =
                GridLayoutManager(requireContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false)
            categoryRv.layoutManager = layoutManager
            categoryRv.adapter = categoryAdapter
            categoryRv.addItemDecoration(
                GridSpaceItemDecoration(
                    spanCount = SPAN_COUNT,
                    spacing = resources.getDimension(R.dimen.recycler_view_category_offset)
                        .toInt()
                )
            )
            categoryRv.addOnItemTouchListener(object : OnItemTouchListener {
                override fun onInterceptTouchEvent(
                    recyclerView: RecyclerView,
                    motionEvent: MotionEvent
                ): Boolean {
                    if (motionEvent.action != MotionEvent.ACTION_UP) {
                        return false
                    }
                    val child =
                        recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
                    if (child == null) {
                        categoryAdapter.enableEditMode(false)
                        return true
                    }
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { /* do nothing */
                }

                override fun onTouchEvent(
                    recyclerView: RecyclerView,
                    motionEvent: MotionEvent
                ) { /* do nothing */
                }
            })
        }
        (viewModel as BaseCategoryViewModel<*>).categories.observe(
            this.viewLifecycleOwner
        ) { list ->
            val sortedList = list.sortedWith(
                compareBy(
                    { it.children.isEmpty() },
                    { -it.usageCount },
                    { it.name }
                )
            )
            if (list.isEmpty()) {
                showViewStub()
            } else {
                hideViewStub()
            }
            categoryAdapter.updateItems(sortedList)
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding.categoryToolbarContainer.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun showViewStub() {
        viewStub?.let {
            it.root.show()
            it.emptyPlaceholderIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_categories_empty
                )
            )
            it.emptyPlaceholderTitle.text =
                getString(R.string.category_empty_placeholder_title)
            it.emptyPlaceholderSubtitle.show()
            it.emptyPlaceholderSubtitle.text =
                getString(R.string.category_empty_placeholder_subtitle)
            it.emptyPlaceholderAdd.hide()
        }
    }

    private fun hideViewStub() {
        viewStub?.root?.gone()
    }

    override fun onBackPressed(): Boolean {
        return if (categoryAdapter.editModeEnable) {
            categoryAdapter.enableEditMode(false)
            false
        } else {
            true
        }
    }

    companion object {
        const val SPAN_COUNT = 4
        const val ARG_CATEGORY = "arg_category"
    }
}

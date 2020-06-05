package com.d9tilov.moneymanager.category.common

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BaseNavigator
import com.d9tilov.moneymanager.base.ui.recyclerview.SpaceItemDecoration
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.CategoryModifyAdapter
import com.d9tilov.moneymanager.core.ui.recyclerview.SimpleItemTouchHelperCallback
import com.d9tilov.moneymanager.core.util.events.OnBackPressed
import com.d9tilov.moneymanager.core.util.events.OnItemClickListener
import com.d9tilov.moneymanager.core.util.events.OnItemLongClickListener
import com.d9tilov.moneymanager.core.util.events.OnItemSwapListener
import com.d9tilov.moneymanager.databinding.FragmentCategoryBinding

abstract class BaseCategoryFragment<N : BaseNavigator, V : BaseCategoryViewModel<N>> :
    BaseFragment<FragmentCategoryBinding, N, V>(),
    OnBackPressed {

    protected abstract fun getToolbarTitle(): String

    protected lateinit var categoryAdapter: CategoryModifyAdapter
    private lateinit var toolbar: Toolbar

    override fun getLayoutId() = R.layout.fragment_category
    override fun performDataBinding(view: View): FragmentCategoryBinding =
        FragmentCategoryBinding.bind(view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryModifyAdapter()
        categoryAdapter.itemClickListener = onItemClickListener
        categoryAdapter.itemLongClickListener = onItemLongClickListener
        categoryAdapter.itemRemoveClickListener = onItemRemoveClickListener
        categoryAdapter.itemSwapListener = onItemSwapListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        with(viewBinding) {
            categoryRv.layoutManager =
                GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
            categoryRv.adapter = categoryAdapter
            val callback = SimpleItemTouchHelperCallback(categoryAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(categoryRv)
            categoryRv.addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.recycler_view_category_offset
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
            this.viewLifecycleOwner,
            Observer { categoryAdapter.updateItems(it) }
        )
    }

    private fun initToolbar() {
        toolbar = viewBinding.categoryToolbarContainer.toolbar
        toolbar.title = getToolbarTitle()
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
    }

    private val onItemClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            viewModel.onCategoryClicked(item)
            Log.d("moggot", "onClick")
        }
    }

    private val onItemLongClickListener = object : OnItemLongClickListener<Category> {
        override fun onItemLongClick(item: Category, position: Int) {
            categoryAdapter.enableEditMode(true)
        }
    }

    private val onItemRemoveClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            viewModel.onCategoryRemoved(item)
        }
    }

    private val onItemSwapListener = object : OnItemSwapListener<Category> {
        override fun onItemSwap(items: List<Category>) {
            viewModel.onItemSwap(items)
        }
    }

    override fun onBackPressed(): Boolean {
        return if (categoryAdapter.editModeEnable) {
            categoryAdapter.enableEditMode(false)
            true
        } else {
            findNavController().popBackStack()
            false
        }
    }
}

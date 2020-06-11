package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.base.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.base.ui.recyclerview.SpaceItemDecoration
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.CategoryAdapter
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinButton
import com.d9tilov.moneymanager.core.ui.widget.currencyview.PinKeyboard
import com.d9tilov.moneymanager.core.util.events.OnItemClickListener
import com.d9tilov.moneymanager.core.util.toBigDecimal
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import com.d9tilov.moneymanager.transaction.ui.TransactionAdapter
import kotlinx.android.synthetic.main.keyboard_layout.view.pin_keyboard

class ExpenseFragment :
    BaseFragment<FragmentExpenseBinding, ExpenseNavigator, ExpenseViewModel>(R.layout.fragment_expense),
    ExpenseNavigator {

    private val categoryAdapter = CategoryAdapter()
    private val transactionAdapter = TransactionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemClickListener = onItemClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            transactionMainContent.pin_keyboard.setClickPinButton(PinClickListener())
        }
        initCategoryRecyclerView()
        initTransactionsRecyclerView()
        viewModel.categories.observe(
            this.viewLifecycleOwner,
            Observer { categoryAdapter.updateItems(it) }
        )
        viewModel.transactions.observe(
            this.viewLifecycleOwner, Observer { transactionAdapter.updateItems(it) }
        )
    }

    private fun initCategoryRecyclerView() {
        viewBinding?.run {
            expenseCategoryRvList.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            expenseCategoryRvList.adapter = categoryAdapter
            expenseCategoryRvList.addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.recycler_view_category_offset
                )
            )
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
    }

    private fun initTransactionsRecyclerView() {
        viewBinding?.run {
            expenseTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            expenseTransactionRvList.adapter = transactionAdapter
        }
    }

    inner class PinClickListener : PinKeyboard.ClickPinButton {
        override fun onPinClick(pinButton: PinButton) {
            viewBinding?.run {
                when (val child = pinButton.getChildAt(0)) {
                    is TextView -> {
                        val digit = child.text.toString()
                        viewModel.updateNum(expenseMainSum.getSum())
                        expenseMainSum.setSum(viewModel.tapNum(digit))
                    }
                    is ImageView -> {
                        viewModel.updateNum(expenseMainSum.getSum())
                        expenseMainSum.setSum(viewModel.removeNum())
                    }
                }
            }
        }
    }

    private val onItemClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            if (item.id == Category.ALL_ITEMS_ID) {
                viewModel.openAllCategories()
            } else {
                viewModel.saveTransaction(item, viewBinding?.expenseMainSum?.getSum().toBigDecimal)
                viewBinding?.expenseMainSum?.setSum("0")
            }
        }
    }

    override fun performDataBinding(view: View): FragmentExpenseBinding =
        FragmentExpenseBinding.bind(view)

    override fun getViewModelClass() = ExpenseViewModel::class.java
    override fun getNavigator() = this
    override fun openCategoriesScreen() {
        findNavController().navigate(R.id.action_mainFragment_to_category_dest)
    }
}

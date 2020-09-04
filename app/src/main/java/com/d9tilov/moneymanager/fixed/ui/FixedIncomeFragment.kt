package com.d9tilov.moneymanager.fixed.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.FixedIncomeNavigator
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.databinding.FragmentFixedIncomeBinding
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.fixed.vm.FixedIncomeViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.TransactionRemoveDialogFragment
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedIncomeFragment :
    BaseFragment<FragmentFixedIncomeBinding, FixedIncomeNavigator>(R.layout.fragment_fixed_income),
    FixedIncomeNavigator {

    private var toolbar: MaterialToolbar? = null
    private var emptyViewStub: ViewStub? = null
    private lateinit var fixedTransactionAdapter: FixedTransactionAdapter

    override fun performDataBinding(view: View) = FragmentFixedIncomeBinding.bind(view)

    override fun getNavigator() = this

    override val viewModel by viewModels<FixedIncomeViewModel>()

    private val onItemClickListener = object : OnItemClickListener<FixedTransaction> {
        override fun onItemClick(item: FixedTransaction, position: Int) {
            val action = FixedIncomeFragmentDirections.toCreateFixedTransactionDest(
                TransactionType.INCOME,
                item
            )
            findNavController().navigate(action)
        }
    }

    private val onCheckBoxClickListener = object : OnItemClickListener<FixedTransaction> {
        override fun onItemClick(item: FixedTransaction, position: Int) {
            viewModel.onCheckClicked(item)
        }
    }

    private val onItemSwipeListener = object : OnItemSwipeListener<FixedTransaction> {
        override fun onItemSwiped(item: FixedTransaction, position: Int) {
            openRemoveConfirmationDialog(item)
        }
    }

    private fun openRemoveConfirmationDialog(transaction: FixedTransaction) {
        val action =
            FixedIncomeFragmentDirections.toRemoveTransactionDialog(fixedTransaction = transaction)
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fixedTransactionAdapter = FixedTransactionAdapter()
        fixedTransactionAdapter.itemClickListener = onItemClickListener
        fixedTransactionAdapter.checkBoxClickListener = onCheckBoxClickListener
        fixedTransactionAdapter.itemSwipeListener = onItemSwipeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            TransactionRemoveDialogFragment.ARG_UNDO_REMOVE_LAYOUT_DISMISS
        )?.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                fixedTransactionAdapter.cancelDeletion()
            }
        }
        viewBinding?.let {
            it.fixedIncomeRvList.adapter = fixedTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            it.fixedIncomeRvList.layoutManager = layoutManager
            it.fixedIncomeRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_fixed_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    fixedTransactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(it.fixedIncomeRvList)
        }
        emptyViewStub = viewBinding?.root?.findViewById(R.id.fixed_income_empty_placeholder)
        viewModel.fixedIncomeList.observe(
            this.viewLifecycleOwner,
            {
                if (it.isEmpty()) {
                    viewBinding?.fixedIncomeRvList?.visibility = GONE
                    showViewStub()
                } else {
                    hideViewStub()
                    viewBinding?.fixedIncomeRvList?.visibility = VISIBLE
                    fixedTransactionAdapter.updateItems(it)
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.prepopulate_menu, menu)
        menu.findItem(R.id.action_skip).isVisible = false
        menu.findItem(R.id.action_add).isVisible = true
    }

    private fun initToolbar() {
        toolbar = viewBinding?.fixedIncomeToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_prepopulate_fixed_incomes)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    openCreatedScreen()
                }
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun showViewStub() {
        if (emptyViewStub?.parent == null) {
            emptyViewStub?.visibility = VISIBLE
        } else {
            val inflatedStub = emptyViewStub?.inflate()
            val stubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_transaction_placeholder_title)
            stubTitle?.text =
                getString(R.string.transaction_empty_placeholder_fixed_income_title)
            val addIncome =
                inflatedStub?.findViewById<ImageView>(R.id.empty_transaction_placeholder_add)
            addIncome?.setOnClickListener {
                openCreatedScreen()
            }
        }
    }

    private fun openCreatedScreen() {
        val action = FixedIncomeFragmentDirections.toCreateFixedTransactionDest(TransactionType.INCOME)
        findNavController().navigate(action)
    }

    private fun hideViewStub() {
        emptyViewStub?.visibility = GONE
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
    }
}

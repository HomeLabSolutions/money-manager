package com.d9tilov.moneymanager.prepopulate.currency.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.databinding.FragmentCurrencyBinding
import com.d9tilov.moneymanager.prepopulate.currency.data.entity.Currency
import com.d9tilov.moneymanager.prepopulate.currency.vm.CurrencyViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyFragment :
    BaseFragment<FragmentCurrencyBinding, CurrencyNavigator>(R.layout.fragment_currency),
    CurrencyNavigator {

    private var toolbar: MaterialToolbar? = null
    private var snackBar: Snackbar? = null
    private lateinit var currencyAdapter: CurrencyAdapter
    override val viewModel by viewModels<CurrencyViewModel>()
    override fun performDataBinding(view: View) = FragmentCurrencyBinding.bind(view)
    override fun getNavigator(): CurrencyNavigator = this

    private val onItemClickListener = object : OnItemClickListener<Currency> {
        override fun onItemClick(item: Currency, position: Int) {
            viewModel.updateBaseCurrency(item)
        }
    }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyAdapter = CurrencyAdapter()
        currencyAdapter.itemClickListener = onItemClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.currencies.observe(
            this.viewLifecycleOwner,
            { list ->
                val sortedList = list.sortedBy { it.code }
                currencyAdapter.updateItems(sortedList)
                val checkedIndex = sortedList.indexOfFirst { it.isBase }
                viewBinding?.currencyRv?.scrollToPosition(checkedIndex)
            }
        )
        viewBinding?.currencyRv?.adapter = currencyAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        viewBinding?.currencyRv?.layoutManager = layoutManager
        (viewBinding?.currencyRv?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.prepopulate_menu, menu)
    }

    private fun initToolbar() {
        toolbar = viewBinding?.currencyToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_prepopulate_currency)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_skip -> {
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.ITEM_ID, "skip_prepopulation")
                    }
                }
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun goToCommonAmountScreen() {
        TODO("Not yet implemented")
    }

    override fun showError() {
        snackBar?.setActionTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.button_normal_color_start
            )
        )
        snackBar?.setAction(getString(R.string.retry)) { viewModel.retryCall() }?.show()
        snackBar?.view?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.button_normal_color_end
            )
        )
    }
}

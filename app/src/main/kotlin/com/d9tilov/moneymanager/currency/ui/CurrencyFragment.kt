package com.d9tilov.moneymanager.currency.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.currency.domain.entity.CurrencyDestination
import com.d9tilov.moneymanager.currency.vm.CurrencyViewModel
import com.d9tilov.moneymanager.databinding.FragmentCurrencyBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyFragment :
    BaseFragment<CurrencyNavigator, FragmentCurrencyBinding>(
        FragmentCurrencyBinding::inflate,
        R.layout.fragment_currency
    ),
    CurrencyNavigator {

    private val args by navArgs<CurrencyFragmentArgs>()
    private val destination: CurrencyDestination? by lazy { args.destination }
    private val currencyCode: String? by lazy { args.currencyCode }

    private var menu: Menu? = null
    private var toolbar: MaterialToolbar? = null
    private val currencyAdapter: CurrencyAdapter by lazy {
        CurrencyAdapter(
            destination != CurrencyDestination.ProfileCurrentScreen
        ) { item, _ ->
            when (destination ?: CurrencyDestination.PrepopulateScreen) {
                CurrencyDestination.PrepopulateScreen -> viewModel.changeCurrency(item)
                CurrencyDestination.ProfileCurrentScreen -> {
                    val action = CurrencyFragmentDirections.toChangeCurrencyDialog(item)
                    findNavController().navigate(action)
                }
                CurrencyDestination.EditTransactionScreen,
                CurrencyDestination.EditRegularTransactionScreen,
                CurrencyDestination.IncomeExpenseScreen -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        ARG_CURRENCY,
                        item
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }
    override val viewModel by viewModels<CurrencyViewModel>()
    override fun getNavigator(): CurrencyNavigator = this

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewBinding?.run {
            currencyRv.adapter = currencyAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            currencyRv.layoutManager = layoutManager
            (currencyRv.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.currencies()
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { result ->
                        val menuItem = menu?.findItem(R.id.action_skip)
                        when (result) {
                            is ResultOf.Success -> {
                                menuItem?.isEnabled = true
                                val sortedList =
                                    result.data.sortedBy { item ->
                                        CurrencyUtils.getCurrencyFullName(
                                            item.code
                                        )
                                    }
                                        .map { currency ->
                                            if (currencyCode != null) {
                                                currency.copy(isBase = (currencyCode == currency.code))
                                            } else currency
                                        }
                                currencyAdapter.updateItems(sortedList)
                                val checkedIndex = sortedList.indexOfFirst { it.isBase }
                                currencyRv.scrollToPosition(checkedIndex)
                                currencyProgress.gone()
                            }
                            is ResultOf.Failure -> {
                                menuItem?.isEnabled = true
                                showError(result.throwable!!)
                                currencyProgress.gone()
                            }
                            is ResultOf.Loading -> {
                                menuItem?.isEnabled = false
                                currencyProgress.show()
                            }
                        }
                    }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (destination == null) {
            this.menu = menu
            inflater.inflate(R.menu.prepopulate_menu, menu)
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding?.currencyToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        toolbar?.title = getString(R.string.title_prepopulate_currency)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        if (destination != null) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.setHomeButtonEnabled(true)
        } else {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            activity.supportActionBar?.setHomeButtonEnabled(false)
        }
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_skip -> {
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.ITEM_ID, "skip_prepopulation")
                    }
                    viewModel.createBudgetAndSkip()
                }
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun skip() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.finish()
    }

    override fun showError(throwable: Throwable) {
        val snackBar = viewBinding?.let {
            Snackbar
                .make(
                    it.currencyParentLayout,
                    throwable.message!!,
                    Snackbar.LENGTH_INDEFINITE
                )
        }
        snackBar?.run {
            val value = TypedValue()
            context.theme.resolveAttribute(R.attr.colorError, value, true)
            val backgroundColor = value.data
            view.setBackgroundColor(backgroundColor)
            context.theme.resolveAttribute(R.attr.colorSecondary, value, true)
            val actionTextColor = value.data
            setActionTextColor(actionTextColor).setAction(getString(R.string.retry)) { viewModel.loadCurrencies() }
                .show()
        }
    }

    companion object {
        const val ARG_CURRENCY = "arg_currency"
    }
}

package com.d9tilov.moneymanager.currency.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.currency.domain.entity.CurrencyDestination
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.vm.CurrencyViewModel
import com.d9tilov.moneymanager.databinding.FragmentCurrencyBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
    private val currencyAdapter: CurrencyAdapter by lazy { CurrencyAdapter(destination != CurrencyDestination.PROFILE_SCREEN_CURRENT) }
    override val viewModel by viewModels<CurrencyViewModel>()
    override fun getNavigator(): CurrencyNavigator = this

    private val onItemClickListener = object : OnItemClickListener<DomainCurrency> {
        override fun onItemClick(item: DomainCurrency, position: Int) {
            when (destination ?: CurrencyDestination.PREPOPULATE_SCREEN) {
                CurrencyDestination.PREPOPULATE_SCREEN -> viewModel.changeCurrency(item)
                CurrencyDestination.PROFILE_SCREEN_CURRENT -> {
                    val action = CurrencyFragmentDirections.toChangeCurrencyDialog(item)
                    findNavController().navigate(action)
                }
                CurrencyDestination.EDIT_TRANSACTION_SCREEN,
                CurrencyDestination.EDIT_REGULAR_TRANSACTION_SCREEN,
                CurrencyDestination.INCOME_EXPENSE_SCREEN -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        ARG_CURRENCY,
                        item
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyAdapter.itemClickListener = onItemClickListener
    }

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
                                            if (currencyCode != null)
                                                currency.copy(isBase = (currencyCode == currency.code))
                                            else currency
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
            setMenuTextColor(menu)
        }
    }

    private fun setMenuTextColor(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.action_skip)
        val s = SpannableString(getString(R.string.toolbar_menu_skip))
        s.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.control_activated_color
                )
            ),
            0,
            s.length,
            0
        )
        item.title = s
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
            view.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.button_normal_color_end
                )
            )
            setActionTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.button_normal_color_start
                )
            ).setAction(getString(R.string.retry)) { viewModel.loadCurrencies() }.show()
        }
    }

    companion object {
        const val ARG_CURRENCY = "arg_currency"
    }
}

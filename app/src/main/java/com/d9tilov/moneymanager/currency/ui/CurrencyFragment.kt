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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.vm.CurrencyViewModel
import com.d9tilov.moneymanager.databinding.FragmentCurrencyBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.prepopulate.ui.ControlsClicked
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyFragment :
    BaseFragment<CurrencyNavigator>(R.layout.fragment_currency),
    CurrencyNavigator,
    ControlsClicked {

    private val viewBinding by viewBinding(FragmentCurrencyBinding::bind)
    private var toolbar: MaterialToolbar? = null
    private var snackBar: Snackbar? = null
    private lateinit var currencyAdapter: CurrencyAdapter
    override val viewModel by viewModels<CurrencyViewModel>()
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
                viewBinding.currencyRv.scrollToPosition(checkedIndex)
            }
        )
        viewBinding.currencyRv.adapter = currencyAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        viewBinding.currencyRv.layoutManager = layoutManager
        (viewBinding.currencyRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.prepopulate_menu, menu)
        setMenuTextColor(menu)
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
        toolbar = viewBinding.currencyToolbarContainer.toolbar
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
                    viewModel.skip()
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

package com.d9tilov.moneymanager.statistics.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsDetailsNavigator
import com.d9tilov.moneymanager.databinding.FragmentStatisticsDetailsBinding
import com.d9tilov.moneymanager.statistics.ui.recycler.StatisticsDetailsAdapter
import com.d9tilov.moneymanager.statistics.vm.StatisticsDetailsViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentStatisticsDetails :
    BaseFragment<StatisticsDetailsNavigator, FragmentStatisticsDetailsBinding>(
        FragmentStatisticsDetailsBinding::inflate,
        R.layout.fragment_statistics_details
    ),
    StatisticsDetailsNavigator {

    private val args by navArgs<FragmentStatisticsDetailsArgs>()
    private val category by lazy { args.category }

    private val adapter = StatisticsDetailsAdapter()
    private var toolbar: MaterialToolbar? = null

    override fun getNavigator() = this
    override val viewModel by viewModels<StatisticsDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            toolbar = viewBinding?.statisticsDetailsToolbar?.toolbar
            initToolbar(toolbar)
            statisticsDetailsRv.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext())
            val dividerItemDecoration = DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
            statisticsDetailsRv.layoutManager = layoutManager
            val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rv_divider)
            dividerDrawable?.let { dividerItemDecoration.setDrawable(it) }
            statisticsDetailsRv.addItemDecoration(dividerItemDecoration)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTransactions()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { list -> adapter.updateItems(list) }
        }
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = category.name
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
    }
}

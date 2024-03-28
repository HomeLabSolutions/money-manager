package com.d9tilov.android.statistics.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.d9tilov.android.common.android.ui.base.BaseFragment
import com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator
import com.d9tilov.android.statistics.ui.recycler.StatisticsDetailsAdapter
import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsViewModel
import com.d9tilov.android.statistics_ui.R
import com.d9tilov.android.statistics_ui.databinding.FragmentStatisticsDetailsBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentStatisticsDetails :
    BaseFragment<StatisticsDetailsNavigator, FragmentStatisticsDetailsBinding>(
        FragmentStatisticsDetailsBinding::inflate,
        R.layout.fragment_statistics_details
    ),
    StatisticsDetailsNavigator {

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
            val dividerDrawable = ContextCompat.getDrawable(requireContext(), com.d9tilov.android.designsystem.R.drawable.rv_divider)
            dividerDrawable?.let { dividerItemDecoration.setDrawable(it) }
            statisticsDetailsRv.addItemDecoration(dividerItemDecoration)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { list -> adapter.updateItems(list) }
        }
    }

    private fun initToolbar(toolbar: Toolbar?) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title = viewModel.category.value.name
    }
}

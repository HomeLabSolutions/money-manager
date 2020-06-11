package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.ui.ViewModelFactory
import com.d9tilov.moneymanager.core.util.toast
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<T : ViewBinding, N : BaseNavigator, V : BaseViewModel<N>>(@LayoutRes layoutId: Int) :
    DaggerFragment(layoutId) {

    private var activity: BaseActivity<*>? = null
    protected var viewBinding: T? = null
    protected lateinit var viewModel: V

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            activity = context
        }
        viewModel = ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
        viewModel.navigator = getNavigator()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = performDataBinding(view)
        initObservers()
    }

    fun isNetworkConnected() = activity?.isNetworkEnabled()

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        activity = null
        super.onDetach()
    }

    @CallSuper
    protected open fun initObservers() {
        viewModel.msg.observe(
            this.viewLifecycleOwner,
            Observer { event ->
                requireContext().toast(event)
            }
        )
        viewModel.loading.observe(
            this.viewLifecycleOwner,
            Observer { show ->
                if (show) showLoading() else hideLoading()
            }
        )
    }

    private fun showLoading() = activity?.showLoading()
    private fun hideLoading() = activity?.hideLoading()

    abstract fun performDataBinding(view: View): T
    abstract fun getViewModelClass(): Class<V>
    abstract fun getNavigator(): N
}

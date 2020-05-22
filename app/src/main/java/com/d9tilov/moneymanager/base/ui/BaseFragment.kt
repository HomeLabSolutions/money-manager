package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.base.ui.navigator.BaseNavigator
import com.d9tilov.moneymanager.core.util.toast
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<T : ViewBinding, V : BaseViewModel<out BaseNavigator>> :
    DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var activity: BaseActivity<*, *>? = null
    protected lateinit var viewBinding: T
    protected lateinit var viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(getViewModelClass())
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = performDataBinding(view)
        initObservers()
    }

    fun isNetworkConnected() = activity?.isNetworkEnabled()

    override fun onDetach() {
        activity = null
        super.onDetach()
    }

    @CallSuper
    protected open fun initObservers() {
        viewModel.msg.observe(this.viewLifecycleOwner, Observer { event ->
            requireContext().toast(event)
        })
        viewModel.loading.observe(this.viewLifecycleOwner, Observer { show ->
            if (show) showLoading() else hideLoading()
        })
    }

    private fun showLoading() = activity?.showLoading()
    private fun hideLoading() = activity?.hideLoading()

    @LayoutRes
    abstract fun getLayoutId(): Int
    abstract fun performDataBinding(view: View): T
    abstract fun getViewModelClass(): Class<V>
    abstract fun getNavigator(): BaseNavigator
}

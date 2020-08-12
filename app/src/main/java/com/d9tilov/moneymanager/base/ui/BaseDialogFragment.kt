package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.toast

abstract class BaseDialogFragment<V : ViewBinding, N : BaseNavigator> :
    AppCompatDialogFragment() {

    @get:LayoutRes
    protected abstract val layoutId: Int
    abstract fun performDataBinding(view: View): V
    abstract fun getNavigator(): N

    protected abstract val viewModel: BaseViewModel<N>
    protected var viewBinding: V? = null

    private var baseActivity: BaseActivity<*>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            baseActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity?.let {
            viewModel.navigator = getNavigator()
        }
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = LayoutInflater.from(requireContext()).inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = performDataBinding(view)
        initObservers()
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

    private fun showLoading() = baseActivity?.showLoading()
    private fun hideLoading() = baseActivity?.hideLoading()
}

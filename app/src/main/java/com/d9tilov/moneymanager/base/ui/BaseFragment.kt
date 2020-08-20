package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.toast

abstract class BaseFragment<T : ViewBinding, N : BaseNavigator>(@LayoutRes layoutId: Int) :
    Fragment(layoutId) {

    abstract fun performDataBinding(view: View): T
    abstract fun getNavigator(): N

    private var baseActivity: BaseActivity<*>? = null
    protected abstract val viewModel: BaseViewModel<N>
    protected var viewBinding: T? = null

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = performDataBinding(view)
        initObservers()
    }

    fun isNetworkConnected() = baseActivity?.isNetworkEnabled()

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        baseActivity = null
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

    protected fun showSnackBar(text: String, gravityCenter: Boolean = false) {
        baseActivity?.let {
            val backgroundTint = if (snackBarBackgroundTint != 0)
                ContextCompat.getColor(it, snackBarBackgroundTint) else 0
            it.showSnackBar(text, backgroundTint, snackBarAnchorView, gravityCenter)
        }
    }

    private fun showLoading() = baseActivity?.showLoading()
    private fun hideLoading() = baseActivity?.hideLoading()

    @get:ColorRes
    protected open val snackBarBackgroundTint = 0
    protected open val snackBarAnchorView: View? = null
}

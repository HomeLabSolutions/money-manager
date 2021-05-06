package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.toast

abstract class BaseFragment<N : BaseNavigator>(@LayoutRes layoutId: Int) :
    Fragment(layoutId) {

    abstract fun getNavigator(): N

    private var baseActivity: BaseActivity<*>? = null
    protected abstract val viewModel: BaseViewModel<N>

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
        initObservers()
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    fun isNetworkConnected() = baseActivity?.isNetworkEnabled()

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    @CallSuper
    protected open fun initObservers() {
        viewModel.getMsg().observe(
            this.viewLifecycleOwner,
            { event -> requireContext().toast(event) }
        )
        viewModel.getLoading().observe(
            this.viewLifecycleOwner,
            { show -> if (show) showLoading() else hideLoading() }
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

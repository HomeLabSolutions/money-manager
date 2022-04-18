package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.ui.BaseViewModel.Companion.DEFAULT_MESSAGE_ID
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.toast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<N : BaseNavigator, VB : ViewBinding>(
    private val inflate: Inflate<VB>,
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {

    protected var viewBinding: VB? = null
    abstract fun getNavigator(): N

    private var baseActivity: BaseActivity<*>? = null
    protected abstract val viewModel: BaseViewModel<N>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) baseActivity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity?.let { viewModel.navigator = getNavigator() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = inflate.invoke(inflater, container, false)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    fun isNetworkConnected() = baseActivity?.isNetworkEnabled()

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    @CallSuper
    protected open fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getMsg().collect { message ->
                        if (message != DEFAULT_MESSAGE_ID) requireContext().toast(message)
                    }
                }
                launch {
                    viewModel.getLoading()
                        .collect { show -> if (show) showLoading() else hideLoading() }
                }
            }
        }
    }

    protected fun showSnackBar(text: String, gravityCenter: Boolean = false) {
        baseActivity?.let {
            val backgroundTint = if (snackBarBackgroundTint != 0)
                ContextCompat.getColor(it, snackBarBackgroundTint) else 0
            it.showSnackBar(text, backgroundTint, snackBarAnchorView, gravityCenter)
        }
    }

    private fun showLoading() {
        if (showCustomLoading()) return
        baseActivity?.showLoading()
    }
    private fun hideLoading() {
        hideCustomLoading()
        baseActivity?.hideLoading()
    }

    @get:ColorRes
    protected open val snackBarBackgroundTint = 0
    protected open val snackBarAnchorView: View? = null
    protected open fun showCustomLoading(): Boolean {
        return false
    }
    protected open fun hideCustomLoading() {}
}

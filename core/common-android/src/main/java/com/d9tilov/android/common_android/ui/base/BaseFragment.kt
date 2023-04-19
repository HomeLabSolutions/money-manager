package com.d9tilov.android.common_android.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.d9tilov.android.common_android.R
import com.d9tilov.android.common_android.ui.base.BaseViewModel.Companion.DEFAULT_MESSAGE_ID
import com.d9tilov.android.common_android.utils.toast
import com.d9tilov.android.designsystem.color.getColorFromAttr
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

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
            val backgroundTint = requireContext().getColorFromAttr(R.attr.colorPrimary)
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

    protected open val snackBarAnchorView: View? = null
    protected open fun showCustomLoading(): Boolean {
        return false
    }
    protected open fun hideCustomLoading() {}
}

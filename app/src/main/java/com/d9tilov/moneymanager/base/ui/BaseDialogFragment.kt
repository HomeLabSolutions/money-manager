package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.toast

abstract class BaseDialogFragment<N : BaseNavigator, VB : ViewBinding>(private val inflate: Inflate<VB>) :
    AppCompatDialogFragment() {

    abstract fun getNavigator(): N

    protected abstract val viewModel: BaseViewModel<N>

    private var baseActivity: BaseActivity<*>? = null
    protected var viewBinding: VB? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) baseActivity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity?.let { viewModel.navigator = getNavigator() }
        isCancelable = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? DialogInterface.OnDismissListener)?.onDismiss(dialog)
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

    @CallSuper
    protected open fun initObservers() {
        viewModel.getMsg().observe(
            this.viewLifecycleOwner
        ) { requireContext().toast(it) }
        viewModel.getLoading().observe(
            this.viewLifecycleOwner
        ) { if (it) showLoading() else hideLoading() }
    }

    private fun showLoading() = baseActivity?.showLoading()
    private fun hideLoading() = baseActivity?.hideLoading()
}

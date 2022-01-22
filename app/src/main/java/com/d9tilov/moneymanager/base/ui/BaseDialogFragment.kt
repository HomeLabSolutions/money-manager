package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.toast

abstract class BaseDialogFragment<N : BaseNavigator> :
    AppCompatDialogFragment() {

    @get:LayoutRes
    protected abstract val layoutId: Int
    abstract fun getNavigator(): N

    protected abstract val viewModel: BaseViewModel<N>

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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? DialogInterface.OnDismissListener)?.onDismiss(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = LayoutInflater.from(requireContext()).inflate(layoutId, container, false)

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

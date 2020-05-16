package com.d9tilov.moneymanager.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.base.ui.navigator.BaseNavigator
import dagger.android.support.DaggerFragment

abstract class BaseFragment<T : ViewBinding, out V : BaseViewModel<out BaseNavigator>> :
    DaggerFragment() {

    protected var activity: BaseActivity<*, *>? = null
    protected lateinit var viewBinding: T
    private lateinit var viewModel: V

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
        return inflater.inflate(getLayoutId(), container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = performDataBinding(view)
        viewModel = getViewModel()
    }

    fun isNetworkConnected() = activity?.isNetworkEnabled()
    fun showLoading() = activity?.showLoading()
    fun hideLoading() = activity?.hideLoading()

    override fun onDetach() {
        activity = null
        super.onDetach()
    }

    abstract fun performDataBinding(view: View): T
    @LayoutRes
    abstract fun getLayoutId(): Int
    abstract fun getViewModel(): V
    abstract fun getNavigator(): BaseNavigator
}

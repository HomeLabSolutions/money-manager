package com.d9tilov.moneymanager.base.ui

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.base.ui.navigator.BaseNavigator
import com.d9tilov.moneymanager.core.util.hideLoadingDialog
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.showLoadingDialog
import com.d9tilov.moneymanager.core.util.toast
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<T : ViewBinding, V : BaseViewModel<out BaseNavigator>> :
    DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var viewBinding: T
    private lateinit var viewModel: V
    private var progress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(getViewModelClass())
        initObservers()
        viewBinding = performDataBinding()
        setContentView(viewBinding.root)
    }

    abstract fun performDataBinding(): T
    abstract fun getNavigator(): BaseNavigator
    abstract fun getViewModelClass(): Class<V>

    /**
     * When a message event is thrown, handle it and show it
     */
    @CallSuper
    protected open fun initObservers() {
        viewModel.msg.observe(this, Observer { event ->
            toast(event)
        })
        viewModel.loading.observe(this, Observer { show ->
            if (show) showLoading() else hideLoading()
        })
    }

    fun isNetworkEnabled() = isNetworkConnected(this)

    fun showLoading() {
        hideLoading()
        progress = showLoadingDialog(this, viewBinding.root)
    }

    fun hideLoading() {
        if (null != progress) {
            hideLoadingDialog(viewBinding.root as ViewGroup, progress?.parent as ViewGroup)
            progress = null
        }
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}

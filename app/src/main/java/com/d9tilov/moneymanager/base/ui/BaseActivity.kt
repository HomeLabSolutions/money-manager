package com.d9tilov.moneymanager.base.ui

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.base.ui.navigator.BaseNavigator
import com.d9tilov.moneymanager.core.util.hideLoadingDialog
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.showLoadingDialog
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<T : ViewBinding, out V : BaseViewModel<out BaseNavigator>> :
    DaggerAppCompatActivity() {

    protected lateinit var viewBinding: T
    private lateinit var navigator: BaseNavigator
    private var viewModel: V? = null
    private var progress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = performDataBinding()
        setContentView(viewBinding.root)
        navigator = getNavigator()
    }

    abstract fun performDataBinding(): T
    abstract fun getNavigator(): BaseNavigator

    /**
     * When a message event is thrown, handle it and show it
     */
    protected open fun initObservers() {
        viewModel?.message?.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
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

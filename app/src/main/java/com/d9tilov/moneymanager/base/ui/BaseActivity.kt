package com.d9tilov.moneymanager.base.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.util.events.OnBackPressed
import com.d9tilov.moneymanager.core.util.hideLoadingDialog
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.showLoadingDialog
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<T : ViewBinding> : DaggerAppCompatActivity() {

    protected lateinit var viewBinding: T
    private var progress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = performDataBinding()
        setContentView(viewBinding.root)
    }

    abstract fun performDataBinding(): T

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val navHostFragment =
            this.supportFragmentManager.findFragmentById(R.id.mainNavGraph)
        var currentFragment: Fragment? = null
        navHostFragment?.let {
            currentFragment = it.childFragmentManager.fragments[0]
        }
        (currentFragment as? OnBackPressed)?.onBackPressed() ?: super.onBackPressed()
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}

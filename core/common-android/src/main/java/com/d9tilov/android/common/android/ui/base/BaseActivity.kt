package com.d9tilov.android.common.android.ui.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.d9tilov.android.common.android.utils.showLoadingDialog
import com.d9tilov.android.common.android.utils.hideLoadingDialog
import com.d9tilov.android.common.android.BuildConfig
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected open val navHostFragmentId: Int = -1
    protected var viewBinding: T? = null
    private var progress: ProgressBar? = null
    private val contentLayout: ViewGroup by lazy { findViewById<ViewGroup>(android.R.id.content) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = performDataBinding()
        setContentView(viewBinding!!.root)
        if (BuildConfig.DEBUG) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    abstract fun performDataBinding(): T

    fun showLoading() {
        hideLoading()
        progress = showLoadingDialog(this, viewBinding?.root)
    }

    fun hideLoading() {
        if (null != progress) {
            hideLoadingDialog(viewBinding?.root as ViewGroup, progress?.parent as ViewGroup)
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

    protected fun getCurrentFragment(): Fragment? {
        val navHostFragment =
            this.supportFragmentManager.findFragmentById(navHostFragmentId)
        var currentFragment: Fragment? = null
        navHostFragment?.let {
            currentFragment = it.childFragmentManager.fragments[0]
        }
        return currentFragment
    }

    fun showSnackBar(
        text: String,
        @ColorInt backgroundTint: Int = 0,
        anchorView: View? = null
    ) {
        val snackBar = Snackbar.make(contentLayout, text, Snackbar.LENGTH_LONG)
        if (backgroundTint != 0) {
            snackBar.setBackgroundTint(backgroundTint)
        }
        snackBar.anchorView = anchorView
        snackBar.show()
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}

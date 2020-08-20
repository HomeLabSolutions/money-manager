package com.d9tilov.moneymanager.base.ui

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnBackPressed
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.util.hideLoadingDialog
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.px
import com.d9tilov.moneymanager.core.util.showLoadingDialog
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected lateinit var viewBinding: T
    private var progress: ProgressBar? = null
    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener
    private lateinit var topView: View
    private var isKeyboardShown = false
    private val contentLayout: ViewGroup by lazy { findViewById<ViewGroup>(android.R.id.content) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = performDataBinding()
        setContentView(viewBinding.root)
        topView = window.decorView.findViewById<View>(android.R.id.content)
        globalLayoutListener = KeyboardGlobalLayoutListener()
        topView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
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

    override fun onDestroy() {
        topView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        super.onDestroy()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        (currentFragment as? OnBackPressed)?.onBackPressed() ?: super.onBackPressed()
    }

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment =
            this.supportFragmentManager.findFragmentById(R.id.nav_host_container)
        var currentFragment: Fragment? = null
        navHostFragment?.let {
            currentFragment = it.childFragmentManager.fragments[0]
        }
        return currentFragment
    }

    open fun onOpenKeyboard() {}
    open fun onCloseKeyboard() {}

    inner class KeyboardGlobalLayoutListener : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val heightDifference = topView.rootView.height - topView.height
            val currentFragment = getCurrentFragment()
            if (heightDifference > 200.px) {
                if (isKeyboardShown) {
                    return
                }
                isKeyboardShown = true
                onOpenKeyboard()
                (currentFragment as? OnKeyboardVisibleChange)?.onOpenKeyboard()
            } else {
                if (!isKeyboardShown) {
                    return
                }
                isKeyboardShown = false
                onCloseKeyboard()
                (currentFragment as? OnKeyboardVisibleChange)?.onCloseKeyboard()
            }
        }
    }

    fun showSnackBar(
        text: String,
        @ColorInt backgroundTint: Int = 0,
        anchorView: View? = null,
        gravityCenter: Boolean = false
    ) {
        val snackBar = Snackbar.make(contentLayout, text, Snackbar.LENGTH_LONG)
        if (backgroundTint != 0) {
            snackBar.setBackgroundTint(backgroundTint)
        }
        snackBar.anchorView = anchorView
        val view: View = snackBar.view
        val tv =
            view.findViewById<View>(R.id.snackbar_text) as TextView
        if (gravityCenter) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            } else {
                tv.gravity = Gravity.CENTER_HORIZONTAL
            }
        }
        snackBar.show()
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}

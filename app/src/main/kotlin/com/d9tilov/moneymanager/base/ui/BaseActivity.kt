package com.d9tilov.moneymanager.base.ui

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.hideLoadingDialog
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.showLoadingDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected open val navHostFragmentId: Int = -1
    protected var viewBinding: T? = null
    private var progress: ProgressBar? = null
    private val contentLayout: ViewGroup by lazy { findViewById<ViewGroup>(android.R.id.content) }

    private val preferenceDataStore: PreferencesStore by lazy { PreferencesStore(this) }
    private var currency = CurrencyMetaData(
        DataConstants.DEFAULT_CURRENCY_CODE,
        DataConstants.DEFAULT_CURRENCY_SYMBOL
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = performDataBinding()
        setContentView(viewBinding!!.root)
        lifecycleScope.launch {
            preferenceDataStore.currentCurrency
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect { currency = it }
        }
        if (BuildConfig.DEBUG) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    abstract fun performDataBinding(): T

    fun isNetworkEnabled() = isNetworkConnected(this)

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

    fun getCurrency() = currency

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}

fun Fragment.currencyCode() = (requireActivity() as BaseActivity<*>).getCurrency().code

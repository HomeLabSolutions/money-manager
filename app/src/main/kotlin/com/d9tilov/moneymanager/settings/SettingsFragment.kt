package com.d9tilov.moneymanager.settings

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.util.debounce
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hide
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.core.util.toBackupDate
import com.d9tilov.moneymanager.databinding.FragmentSettingsBinding
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment :
    BaseFragment<SettingsNavigator, FragmentSettingsBinding>(FragmentSettingsBinding::inflate, R.layout.fragment_settings),
    SettingsNavigator {

    private var toolbar: MaterialToolbar? = null
    private var rotation: Animation? = null

    override fun getNavigator() = this
    override val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewBinding?.run {
            settingsRefresh.setOnClickListener { viewModel.backup() }
            settingsDayOfMonthPostfix.setOnClickListener {
                settingsDayOfMonthEdit.requestFocus()
                showKeyboard(settingsDayOfMonthEdit)
            }
            settingsDayOfMonthEdit.onChange(
                debounce(DEBOUNCE, lifecycleScope) {
                    settingsDayOfMonthEdit.setSelection(it.length)
                    val isError =
                        it.isEmpty() || !it.isDigitsOnly() || it.toInt() > LAST_MAX_DAY_IN_MONTH || it.toInt() < FIRST_MIN_DAY_IN_MONTH
                    if (isError) settingsDayOfMonthError.show()
                    else settingsDayOfMonthError.gone()
                    settingsSave.isEnabled = !isError
                }
            )
            settingsSave.setOnClickListener {
                viewModel.changeFiscalDay(
                    settingsDayOfMonthEdit.text.toString().toInt()
                )
            }
            settingsSubscription.root.setOnClickListener {
                findNavController().navigate(
                    SettingsFragmentDirections.toSettingsBillingDest()
                )
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.userData.collect { data: UserProfile? ->
                            settingsDayOfMonthEdit.setText(data?.fiscalDay.toString())
                        }
                    }
                    launch {
                        viewModel.backupData.collect { data ->
                            if (data.lastBackupTimestamp == BackupData.UNKNOWN_BACKUP_DATE) {
                                settingsBackupInfo.setText(R.string.settings_backup_empty)
                            } else {
                                settingsBackupInfo.text =
                                    getString(
                                        R.string.settings_backup_info,
                                        data.lastBackupTimestamp.toBackupDate()
                                    )
                            }
                        }
                    }
                    launch {
                        viewModel.canPurchase.collect { canPurchase ->
                            if (canPurchase) settingsSubscription.root.show()
                            else settingsSubscription.root.gone()
                        }
                    }
                    launch {
                        viewModel.minBillingPrice.collect { price ->
                            settingsSubscription.settingsSubscriptionPrice.text = getString(
                                R.string.settings_subscription_premium_min_price,
                                price.symbol,
                                price.value,
                                price.code
                            )
                        }
                    }
                    launch {
                        viewModel.isPremium.collect { isPremium ->
                            settingsSubscription.root.isEnabled = !isPremium
                            if (isPremium) {
                                settingsSubscription.settingsSubscriptionTitle.text =
                                    getString(R.string.settings_subscription_premium_acknowledged_title)
                                settingsSubscription.settingsSubscriptionPrice.hide()
                            } else {
                                settingsSubscription.settingsSubscriptionTitle.text =
                                    getString(R.string.settings_subscription_premium_title)
                                settingsSubscription.settingsSubscriptionDescription.text =
                                    getString(R.string.settings_subscription_premium_description)
                                settingsSubscription.settingsSubscriptionPrice.show()
                            }
                        }
                    }
                    launch {
                        viewModel.getActiveSku.collect { hasRenewablePremium ->
                            settingsSubscription.settingsSubscriptionDescription.text =
                                getString(
                                    if (hasRenewablePremium) R.string.settings_subscription_premium_acknowledged_subtitle_renewing
                                    else R.string.settings_subscription_premium_acknowledged_subtitle_cancel
                                )
                        }
                    }
                }
            }
        }
    }

    private fun startRotation() {
        rotation = AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate)
        rotation?.fillAfter = true
        viewBinding?.settingsRefresh?.startAnimation(rotation)
    }

    private fun stopRotation() {
        viewBinding?.settingsRefresh?.clearAnimation()
    }

    override fun showCustomLoading(): Boolean {
        startRotation()
        return true
    }

    override fun hideCustomLoading() {
        stopRotation()
    }

    override fun onStop() {
        super.onStop()
        stopRotation()
    }

    private fun initToolbar() {
        toolbar = viewBinding?.settingsToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        toolbar?.title = getString(R.string.title_settings)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun save() {
        findNavController().popBackStack()
    }

    companion object {
        private const val DEBOUNCE = 300L
        private const val LAST_MAX_DAY_IN_MONTH = 31
        private const val FIRST_MIN_DAY_IN_MONTH = 1
    }
}

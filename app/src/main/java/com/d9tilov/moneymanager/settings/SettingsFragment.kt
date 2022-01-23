package com.d9tilov.moneymanager.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.debounce
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.onChange
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.core.util.toBackupDate
import com.d9tilov.moneymanager.databinding.FragmentSettingsBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment :
    BaseFragment<SettingsNavigator>(R.layout.fragment_settings),
    SettingsNavigator {

    private var toolbar: MaterialToolbar? = null
    private val viewBinding by viewBinding(FragmentSettingsBinding::bind)

    override fun getNavigator() = this

    override val viewModel by viewModels<SettingsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewBinding.run {
            settingsRefresh.setOnClickListener { viewModel.backup() }
            settingsDayOfMonthPostfix.setOnClickListener {
                settingsDayOfMonthEdit.requestFocus()
                showKeyboard(settingsDayOfMonthEdit)
            }
            settingsDayOfMonthEdit.onChange(
                debounce(DEBOUNCE, lifecycleScope) {
                    settingsDayOfMonthEdit.setSelection(it.length)
                    val isError =
                        it.isEmpty() || !it.isDigitsOnly() || it.toInt() > 31 || it.toInt() < 1
                    if (isError) {
                        settingsDayOfMonthError.show()
                    } else {
                        settingsDayOfMonthError.gone()
                    }
                    settingsSave.isEnabled = !isError
                }
            )
            settingsSave.setOnClickListener {
                viewModel.changeFiscalDay(
                    settingsDayOfMonthEdit.text.toString().toInt()
                )
            }
        }
        viewModel.userData.observe(
            viewLifecycleOwner,
            {
                viewBinding.settingsDayOfMonthEdit.setText(it.fiscalDay.toString())
                if (it.backupData.lastBackupTimestamp == 0L) {
                    viewBinding.settingsBackupInfo.setText(R.string.settings_backup_empty)
                } else {
                    viewBinding.settingsBackupInfo.text =
                        getString(
                            R.string.settings_backup_info,
                            it.backupData.lastBackupTimestamp.toBackupDate()
                        )
                }
            }
        )
    }

    private fun initToolbar() {
        toolbar = viewBinding.settingsToolbarContainer.toolbar
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
    }
}

package com.d9tilov.moneymanager.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
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
        viewBinding.settingsBackup.setOnClickListener {
            viewModel.backup()
        }
        viewModel.backupData.observe(
            viewLifecycleOwner,
            {
                if (it.lastBackupTimestamp == 0L) {
                    viewBinding.settingsBackupInfo.setText(R.string.backup_empty)
                } else {
                    viewBinding.settingsBackupInfo.text =
                        getString(
                            R.string.backup_info,
                            it.lastBackupTimestamp.toBackupDate()
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
}

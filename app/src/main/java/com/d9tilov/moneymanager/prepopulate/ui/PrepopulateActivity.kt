package com.d9tilov.moneymanager.prepopulate.ui

import com.d9tilov.moneymanager.base.ui.BaseActivity
import com.d9tilov.moneymanager.databinding.ActivityPrepopulateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepopulateActivity : BaseActivity<ActivityPrepopulateBinding>() {
    override fun performDataBinding() = ActivityPrepopulateBinding.inflate(layoutInflater)
}

package com.d9tilov.android.settings.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.d9tilov.android.billing.domain.model.BillingSkuDetails
import com.d9tilov.android.billing.domain.model.BillingSkuDetails.Companion.TAG_ANNUAL
import com.d9tilov.android.billing.domain.model.BillingSkuDetails.Companion.TAG_QUARTERLY
import com.d9tilov.android.common_android.ui.base.BaseFragment
import com.d9tilov.android.common_android.utils.ZoomOutPageTransformer
import com.d9tilov.android.settings.ui.adapter.DotIndicatorPager2Adapter
import com.d9tilov.android.settings.ui.navigation.SettingsBillingNavigator
import com.d9tilov.android.settings.ui.vm.SettingsBillingIntroViewModel
import com.d9tilov.android.settings_ui.R
import com.d9tilov.android.settings_ui.databinding.FragmentBillingIntroBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsBillingIntroFragment :
    BaseFragment<SettingsBillingNavigator, FragmentBillingIntroBinding>(
        FragmentBillingIntroBinding::inflate,
        R.layout.fragment_billing_intro
    ),
    SettingsBillingNavigator {

    private var toolbar: MaterialToolbar? = null

    override fun getNavigator(): SettingsBillingNavigator = this

    override val viewModel by viewModels<SettingsBillingIntroViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewBinding?.run {
            val adapter = DotIndicatorPager2Adapter()
            settingsBillingIntroViewpager.adapter = adapter

            val zoomOutPageTransformer = ZoomOutPageTransformer()
            settingsBillingIntroViewpager.setPageTransformer { page, position ->
                zoomOutPageTransformer.transformPage(page, position)
            }

            settingsBillingIntroDots.attachTo(settingsBillingIntroViewpager)
            settingsBillingIntroBuy.setOnClickListener {
                val skuType = when (settingsBillingIntroRadioGroup.checkedRadioButtonId) {
                    R.id.settings_billing_intro_radio_quarterly -> TAG_QUARTERLY
                    R.id.settings_billing_intro_radio_annual -> TAG_ANNUAL
                    else -> throw IllegalArgumentException("Wrong radio button id: ${settingsBillingIntroRadioGroup.checkedRadioButtonId}")
                }
                viewModel.buySubscription(skuType) { billingClient, paramBuilder ->
                    billingClient.launchBillingFlow(requireActivity(), paramBuilder)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.skuDetails.collect { skuDetails: List<BillingSkuDetails> ->
                            for (sku in skuDetails) {
                                when (sku.tag) {
                                    TAG_QUARTERLY ->
                                        settingsBillingIntroRadioQuarterlyPrice.text = getString(
                                            R.string.billing_purchase_quarterly_price,
                                            sku.price.symbol,
                                            sku.price.value,
                                            sku.price.code
                                        )
                                    TAG_ANNUAL ->
                                        settingsBillingIntroRadioAnnualPrice.text = getString(
                                            R.string.billing_purchase_annual_price,
                                            sku.price.symbol,
                                            sku.price.value,
                                            sku.price.code
                                        )
                                    else -> throw IllegalArgumentException("Wrong sku tag: ${sku.tag}")
                                }
                            }
                        }
                    }
                    launch { viewModel.purchaseCompleted.collect { completed -> if (completed) findNavController().popBackStack() } }
                }
            }
        }
    }

    private fun initToolbar() {
        toolbar = viewBinding?.settingsBillingIntroToolbarContainer?.toolbar
        val activity = activity as AppCompatActivity
        toolbar?.title = getString(R.string.billing_title)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}

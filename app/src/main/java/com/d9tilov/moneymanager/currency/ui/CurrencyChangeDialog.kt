package com.d9tilov.moneymanager.currency.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyChangeNavigator
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.vm.CurrencyChangeViewModel
import com.d9tilov.moneymanager.databinding.FragmentDialogChangeCurrencyBinding
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyChangeDialog :
    BaseDialogFragment<CurrencyChangeNavigator, FragmentDialogChangeCurrencyBinding>(
        FragmentDialogChangeCurrencyBinding::inflate
    ),
    CurrencyChangeNavigator {

    private val args by navArgs<CurrencyChangeDialogArgs>()
    private val currency: DomainCurrency by lazy { args.currency }

    override fun getNavigator(): CurrencyChangeNavigator = this

    override val viewModel by viewModels<CurrencyChangeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            changeButtonConfirm.setOnClickListener { viewModel.changeCurrency(currency) }
            changeButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return super.onCreateDialog(savedInstanceState)
    }

    override fun change() {
        startActivity(
            Intent(context, SplashActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        requireActivity().finish()
    }
}

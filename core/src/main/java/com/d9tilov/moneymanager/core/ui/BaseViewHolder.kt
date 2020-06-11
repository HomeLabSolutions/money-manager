package com.d9tilov.moneymanager.core.ui

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder(private val dataBinding: ViewBinding) :
    RecyclerView.ViewHolder(dataBinding.root), LayoutContainer {

    protected val context: Context = dataBinding.root.context

    override val containerView: View?
        get() = dataBinding.root
}

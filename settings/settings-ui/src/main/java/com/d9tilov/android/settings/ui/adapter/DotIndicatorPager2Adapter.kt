package com.d9tilov.android.settings.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.d9tilov.android.common_android.ui.base.BaseViewHolder
import com.d9tilov.android.settings_ui.R
import com.d9tilov.android.settings_ui.databinding.ItemBillingIntroCardBinding

class DotIndicatorPager2Adapter : RecyclerView.Adapter<ViewHolder>() {

    data class Card(val image: Image, @StringRes val strId: Int)

    @JvmInline
    value class Image(@DrawableRes val id: Int)

    private val items = listOf(
        Card(Image(R.drawable.ic_billing_1), R.string.billing_intro_1),
        Card(Image(R.drawable.ic_billing_2), R.string.billing_intro_2),
        Card(Image(R.drawable.ic_billing_3), R.string.billing_intro_3),
        Card(Image(R.drawable.ic_billing_4), R.string.billing_intro_4),
        Card(Image(R.drawable.ic_billing_5), R.string.billing_intro_5),
        Card(Image(R.drawable.ic_billing_6), R.string.billing_intro_6)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =
            ItemBillingIntroCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillingPreviewViewHolder(viewBinding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as BillingPreviewViewHolder).bind(items[position])
    }

    class BillingPreviewViewHolder(private val viewBinding: ItemBillingIntroCardBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(card: Card) {
            Glide
                .with(context)
                .load(card.image.id)
                .into(viewBinding.itemBillingCardIcon)
            viewBinding.itemBillingCardDescription.text = context.getString(card.strId)
        }
    }
}

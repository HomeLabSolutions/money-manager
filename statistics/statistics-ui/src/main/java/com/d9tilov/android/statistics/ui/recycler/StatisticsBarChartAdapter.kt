package com.d9tilov.android.statistics.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.android.common_android.ui.base.BaseViewHolder
import com.d9tilov.android.common_android.utils.IMAGE_SIZE_IN_PX
import com.d9tilov.android.common_android.utils.createTintDrawable
import com.d9tilov.android.common_android.utils.let2
import com.d9tilov.android.core.events.OnItemClickListener
import com.d9tilov.android.core.utils.removeScale
import com.d9tilov.android.statistics.ui.recycler.diff.StatisticsBarCharDiffUtils
import com.d9tilov.android.statistics_ui.R
import com.d9tilov.android.statistics_ui.databinding.ItemStatisticsBarChartBinding
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import java.math.BigDecimal
import java.math.RoundingMode

class StatisticsBarChartAdapter(private val transactionClickListener: OnItemClickListener<TransactionChartModel>) :
    RecyclerView.Adapter<StatisticsBarChartAdapter.StatisticsBarChartViewHolder>() {

    private var data = mutableListOf<TransactionChartModel>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatisticsBarChartViewHolder {
        val viewBinding =
            ItemStatisticsBarChartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        val viewHolder = StatisticsBarChartViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                transactionClickListener.onItemClick(data[adapterPosition], adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: StatisticsBarChartViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    override fun getItemId(position: Int) = data[position].category.id

    fun updateItems(newData: List<TransactionChartModel>) {
        val diffUtilsCallback = StatisticsBarCharDiffUtils(data, newData)
        val diffUtilsResult = DiffUtil.calculateDiff(diffUtilsCallback, false)
        data.clear()
        data.addAll(newData)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    class StatisticsBarChartViewHolder(private val viewBinding: ItemStatisticsBarChartBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(item: TransactionChartModel) {
            viewBinding.run {
                itemStatisticsCategoryName.text = item.category.name
                val percent = item.percent.setScale(1, RoundingMode.HALF_UP).removeScale
                itemStatisticsPercent.text = context.getString(
                    R.string.number_with_percent,
                    when {
                        percent < BigDecimal(STATISTICAL_ERROR) -> "<1"
                        percent > BigDecimal(MAX_PERCENT_AMOUNT - STATISTICAL_ERROR) && percent < BigDecimal(
                            MAX_PERCENT_AMOUNT
                        ) -> "${context.getString(R.string.approx_sign)}100"
                        else -> percent.toString()
                    }
                )
                itemStatisticsSum.setValue(item.sum, item.currencyCode)
                val drawable = let2( item.category.icon, item.category.color) { icon, color ->
                    itemStatisticsCategoryName.setTextColor(ContextCompat.getColor(context, color))
                    itemStatisticsProgress.setProgress(item.percent.toFloat(), ContextCompat.getColor(context, color))
                    createTintDrawable(context, icon, color)
                }
                Glide
                    .with(context)
                    .load(drawable)
                    .apply(RequestOptions().override(IMAGE_SIZE_IN_PX, IMAGE_SIZE_IN_PX))
                    .into(itemStatisticsIcon)
            }
        }

        companion object {
            private const val MAX_PERCENT_AMOUNT = 100
            private const val STATISTICAL_ERROR = 0.01
        }
    }
}

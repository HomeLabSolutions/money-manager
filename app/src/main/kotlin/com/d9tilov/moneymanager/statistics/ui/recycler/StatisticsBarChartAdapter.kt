package com.d9tilov.moneymanager.statistics.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.core.util.removeScale
import com.d9tilov.moneymanager.databinding.ItemStatisticsBarChartBinding
import com.d9tilov.moneymanager.statistics.ui.recycler.diff.StatisticsBarCharDiffUtils
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import java.math.BigDecimal
import java.math.RoundingMode

class StatisticsBarChartAdapter :
    RecyclerView.Adapter<StatisticsBarChartAdapter.StatisticsBarChartViewHolder>() {

    var transactionClickListener: OnItemClickListener<TransactionChartModel>? = null

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
                transactionClickListener?.onItemClick(data[adapterPosition], adapterPosition)
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
                itemStatisticsCategoryName.setTextColor(
                    ContextCompat.getColor(context, item.category.color)
                )
                val percent = item.percent.setScale(1, RoundingMode.HALF_UP).removeScale
                itemStatisticsPercent.text = context.getString(
                    R.string.number_with_percent,
                    when {
                        percent < BigDecimal(0.01) -> "<1"
                        percent > BigDecimal(99.99) && percent < BigDecimal(100) -> "${context.getString(R.string.approx_sign)}100"
                        else -> percent.toString()
                    }
                )
                itemStatisticsSum.setValue(item.sum, item.currencyCode)
                itemStatisticsProgress.setProgress(
                    item.percent.toFloat(),
                    ContextCompat.getColor(context, item.category.color)
                )
                val drawable = createTintDrawable(
                    context,
                    item.category.icon,
                    item.category.color
                )
                GlideApp
                    .with(context)
                    .load(drawable)
                    .apply(RequestOptions().override(IMAGE_SIZE_IN_PX, IMAGE_SIZE_IN_PX))
                    .into(itemStatisticsIcon)
            }
        }

        companion object {
            private const val IMAGE_SIZE_IN_PX = 136
        }
    }
}

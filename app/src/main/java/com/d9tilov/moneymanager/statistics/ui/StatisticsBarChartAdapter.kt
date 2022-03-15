package com.d9tilov.moneymanager.statistics.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.core.util.removeScale
import com.d9tilov.moneymanager.databinding.ItemStatisticsBarChartBinding
import com.d9tilov.moneymanager.statistics.ui.diff.StatisticsBarCharDiffUtils
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import java.math.RoundingMode

class StatisticsBarChartAdapter :
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
        return StatisticsBarChartViewHolder(viewBinding)
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
                itemStatisticsPercent.text = context.getString(
                    R.string.number_with_percent,
                    item.percent.setScale(1, RoundingMode.HALF_UP).removeScale.toString()
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

package com.d9tilov.moneymanager.category.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.ui.BaseViewHolder
import com.d9tilov.moneymanager.core.util.createTintDrawable
import com.d9tilov.moneymanager.core.util.glide.GlideApp
import com.d9tilov.moneymanager.databinding.ItemCategoryIconBinding

class CategoryIconSetAdapter :
    RecyclerView.Adapter<CategoryIconSetAdapter.CategoryIconViewHolder>() {

    var itemClickListener: OnItemClickListener<Int>? = null

    private val categoryList = listOf(
        R.drawable.ic_category_backery,
        R.drawable.ic_category_barber_shop,
        R.drawable.ic_category_barista,
        R.drawable.ic_category_beach,
        R.drawable.ic_category_beauty,
        R.drawable.ic_category_beer,
        R.drawable.ic_category_boxing,
        R.drawable.ic_category_brain,
        R.drawable.ic_category_business,
        R.drawable.ic_category_bycicle,
        R.drawable.ic_category_cafe,
        R.drawable.ic_category_car,
        R.drawable.ic_category_car_repair,
        R.drawable.ic_category_car_service,
        R.drawable.ic_category_clothes,
        R.drawable.ic_category_coffee,
        R.drawable.ic_category_coffee2,
        R.drawable.ic_category_cosmetics,
        R.drawable.ic_category_deal,
        R.drawable.ic_category_dentist,
        R.drawable.ic_category_doctor,
        R.drawable.ic_category_doctor2,
        R.drawable.ic_category_doctor3,
        R.drawable.ic_category_electric_train,
        R.drawable.ic_category_electricity,
        R.drawable.ic_category_epilation,
        R.drawable.ic_category_fastfood,
        R.drawable.ic_category_food,
        R.drawable.ic_category_fuel,
        R.drawable.ic_category_garbage,
        R.drawable.ic_category_gift,
        R.drawable.ic_category_gift2,
        R.drawable.ic_category_gift3,
        R.drawable.ic_category_grocery,
        R.drawable.ic_category_group,
        R.drawable.ic_category_gym,
        R.drawable.ic_category_gym2,
        R.drawable.ic_category_hair_dryer,
        R.drawable.ic_category_haircut,
        R.drawable.ic_category_haircut2,
        R.drawable.ic_category_haircut_female,
        R.drawable.ic_category_haircut_modern,
        R.drawable.ic_category_help,
        R.drawable.ic_category_hike,
        R.drawable.ic_category_home,
        R.drawable.ic_category_home_relax,
        R.drawable.ic_category_injury,
        R.drawable.ic_category_internet,
        R.drawable.ic_category_internet2,
        R.drawable.ic_category_investment,
        R.drawable.ic_category_massage,
        R.drawable.ic_category_massage2,
        R.drawable.ic_category_motorbyke,
        R.drawable.ic_category_part_time_job,
        R.drawable.ic_category_percent,
        R.drawable.ic_category_phone,
        R.drawable.ic_category_phone2,
        R.drawable.ic_category_phone3,
        R.drawable.ic_category_phone4,
        R.drawable.ic_category_pills,
        R.drawable.ic_category_pills2,
        R.drawable.ic_category_pills3,
        R.drawable.ic_category_pipette,
        R.drawable.ic_category_plumber,
        R.drawable.ic_category_police,
        R.drawable.ic_category_pomade,
        R.drawable.ic_category_public_transport,
        R.drawable.ic_category_relax,
        R.drawable.ic_category_relax2,
        R.drawable.ic_category_rent,
        R.drawable.ic_category_rent2,
        R.drawable.ic_category_rent3,
        R.drawable.ic_category_salary,
        R.drawable.ic_category_sale,
        R.drawable.ic_category_sale_house,
        R.drawable.ic_category_savings,
        R.drawable.ic_category_shampoo,
        R.drawable.ic_category_sushi,
        R.drawable.ic_category_taxi,
        R.drawable.ic_category_travels,
        R.drawable.ic_category_trip,
        R.drawable.ic_category_turka,
        R.drawable.ic_category_walking,
        R.drawable.ic_category_washing,
        R.drawable.ic_category_water,
        R.drawable.ic_category_wifi,
        R.drawable.ic_category_yoga,
        R.drawable.ic_category_yoga2
    )

    private val colorList = listOf(
        R.color.category_yellow,
        R.color.category_light_green,
        R.color.category_green,
        R.color.category_light_blue,
        R.color.category_blue,
        R.color.category_violet,
        R.color.category_dark_red,
        R.color.category_dark_orange,
        R.color.category_orange,
        R.color.category_light_yellow,
        R.color.category_mint,
        R.color.category_grass_green,
        R.color.category_mud_green,
        R.color.category_lollipop,
        R.color.category_navy_blue,
        R.color.category_flower_violet,
        R.color.category_light_violet,
        R.color.category_pink
    )

    private val categoryColorMap = mutableListOf<Pair<Int, Int>>()

    init {
        setHasStableIds(true)
        categoryList.forEachIndexed { index, icon ->
            categoryColorMap.add(icon to colorList[index % colorList.size])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryIconViewHolder {
        val viewBinding = ItemCategoryIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = CategoryIconViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            val adapterPosition = viewHolder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClick(
                    categoryColorMap[adapterPosition].first,
                    adapterPosition
                )
            }
        }
        return viewHolder
    }

    override fun getItemCount() = categoryColorMap.size

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        holder.bind(categoryColorMap[position])
    }

    class CategoryIconViewHolder(private val viewBinding: ItemCategoryIconBinding) :
        BaseViewHolder(viewBinding) {

        fun bind(categoryItemIcon: Pair<Int, Int>) {
            val tintDrawable =
                createTintDrawable(context, categoryItemIcon.first, categoryItemIcon.second)
            GlideApp
                .with(context)
                .load(tintDrawable)
                .apply(RequestOptions().override(120, 120))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewBinding.categoryItemIcon)
        }
    }

    override fun getItemId(position: Int): Long = categoryColorMap[position].first.toLong()
}

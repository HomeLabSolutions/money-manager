package com.d9tilov.moneymanager.category.ui.vm

import androidx.annotation.DrawableRes
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategorySetNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.category.domain.entity.CategoryGroup
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySetViewModel @Inject constructor(
    private val billingInteractor: BillingInteractor
) :
    BaseViewModel<CategorySetNavigator>() {

    private val groupedIcons = mapOf(
        CategoryGroup.HOUSING to listOf(
            R.drawable.ic_category_electricity,
            R.drawable.ic_category_electricity2,
            R.drawable.ic_category_electricity3,
            R.drawable.ic_category_garbage,
            R.drawable.ic_category_home,
            R.drawable.ic_category_house
        ),
        CategoryGroup.TRANSPORT to listOf(
            R.drawable.ic_category_car,
            R.drawable.ic_category_car_repair,
            R.drawable.ic_category_car_service,
            R.drawable.ic_category_electric_train,
            R.drawable.ic_category_fuel,
            R.drawable.ic_category_motorbyke,
            R.drawable.ic_category_police,
            R.drawable.ic_category_public_transport,
            R.drawable.ic_category_taxi
        ),
        CategoryGroup.FOOD to listOf(
            R.drawable.ic_category_backery,
            R.drawable.ic_category_food,
            R.drawable.ic_category_beer,
            R.drawable.ic_category_barista,
            R.drawable.ic_category_cafe,
            R.drawable.ic_category_coffee,
            R.drawable.ic_category_coffee2,
            R.drawable.ic_category_fastfood,
            R.drawable.ic_category_grocery,
            R.drawable.ic_category_sushi,
            R.drawable.ic_category_water,
            R.drawable.ic_category_turka
        ),
        CategoryGroup.UTILITIES to listOf(
            R.drawable.ic_category_cleaning,
            R.drawable.ic_category_electricity,
            R.drawable.ic_category_electricity2,
            R.drawable.ic_category_electricity3,
            R.drawable.ic_category_garbage,
            R.drawable.ic_category_internet,
            R.drawable.ic_category_internet2,
            R.drawable.ic_category_wifi,
            R.drawable.ic_category_laundry,
            R.drawable.ic_category_laundry2,
            R.drawable.ic_category_phone,
            R.drawable.ic_category_phone2,
            R.drawable.ic_category_phone3,
            R.drawable.ic_category_phone4,
            R.drawable.ic_category_plumber,
            R.drawable.ic_category_camera
        ),
        CategoryGroup.INSURANCE to listOf(
            R.drawable.ic_category_help,
            R.drawable.ic_category_injury,
            R.drawable.ic_category_house_insurance,
            R.drawable.ic_category_contract,
            R.drawable.ic_category_two_people,
            R.drawable.ic_category_law
        ),
        CategoryGroup.MEDICAL to listOf(
            R.drawable.ic_category_dentist,
            R.drawable.ic_category_doctor,
            R.drawable.ic_category_doctor2,
            R.drawable.ic_category_doctor3,
            R.drawable.ic_category_pills,
            R.drawable.ic_category_pills2,
            R.drawable.ic_category_pills3,
            R.drawable.ic_category_pregnant,
            R.drawable.ic_category_heart,
            R.drawable.ic_category_graph
        ),
        CategoryGroup.SPORT to listOf(
            R.drawable.ic_category_boxing,
            R.drawable.ic_category_bycicle,
            R.drawable.ic_category_gym,
            R.drawable.ic_category_gym2,
            R.drawable.ic_category_hike,
            R.drawable.ic_category_sports,
            R.drawable.ic_category_sports2,
            R.drawable.ic_category_sports3,
            R.drawable.ic_category_sports4,
            R.drawable.ic_category_sports5,
            R.drawable.ic_category_sports6,
            R.drawable.ic_category_yoga,
            R.drawable.ic_category_yoga2
        ),
        CategoryGroup.INVESTING to listOf(
            R.drawable.ic_category_business,
            R.drawable.ic_category_deal,
            R.drawable.ic_category_investment,
            R.drawable.ic_category_part_time_job,
            R.drawable.ic_category_percent,
            R.drawable.ic_category_percent2,
            R.drawable.ic_category_keys,
            R.drawable.ic_category_salary,
            R.drawable.ic_category_sale,
            R.drawable.ic_category_sale_house,
            R.drawable.ic_category_savings
        ),
        CategoryGroup.RECREATION to listOf(
            R.drawable.ic_category_beach,
            R.drawable.ic_category_home_relax,
            R.drawable.ic_category_massage,
            R.drawable.ic_category_massage2,
            R.drawable.ic_category_relax,
            R.drawable.ic_category_relax2,
            R.drawable.ic_category_travels,
            R.drawable.ic_category_trip,
            R.drawable.ic_category_walking
        ),
        CategoryGroup.PERSONAL to listOf(
            R.drawable.ic_category_barber_shop,
            R.drawable.ic_category_beauty,
            R.drawable.ic_category_clothes,
            R.drawable.ic_category_cosmetics,
            R.drawable.ic_category_epilation,
            R.drawable.ic_category_hair_dryer,
            R.drawable.ic_category_haircut,
            R.drawable.ic_category_haircut2,
            R.drawable.ic_category_haircut_female,
            R.drawable.ic_category_haircut_modern,
            R.drawable.ic_category_pipette,
            R.drawable.ic_category_pomade,
            R.drawable.ic_category_shampoo,
            R.drawable.ic_category_washing
        ),
        CategoryGroup.OTHERS to listOf(
            R.drawable.ic_category_brain,
            R.drawable.ic_category_group,
            R.drawable.ic_category_gift,
            R.drawable.ic_category_gift2,
            R.drawable.ic_category_gift3,
            R.drawable.ic_category_shield,
            R.drawable.ic_category_shield2,
            R.drawable.ic_category_mask,
            R.drawable.ic_category_flasher,
            R.drawable.ic_category_settings_hand,
            R.drawable.ic_category_password,
            R.drawable.ic_category_key,
            R.drawable.ic_category_scanner,
            R.drawable.ic_category_safe,
            R.drawable.ic_category_people_group
        ),
        CategoryGroup.UNKNOWN to listOf(
            R.drawable.ic_category_food,
            R.drawable.ic_category_grocery,
            R.drawable.ic_category_car,
            R.drawable.ic_category_fuel,
            R.drawable.ic_category_clothes,
            R.drawable.ic_category_doctor,
            R.drawable.ic_category_electricity3,
            R.drawable.ic_category_internet,
            R.drawable.ic_category_investment,
            R.drawable.ic_category_part_time_job,
            R.drawable.ic_category_phone3,
            R.drawable.ic_category_public_transport,
            R.drawable.ic_category_relax,
            R.drawable.ic_category_salary,
            R.drawable.ic_category_sports3
        )
    )

    var isPremium: Boolean = false
        private set

    init {
        viewModelScope.launch {
            billingInteractor.isPremium()
                .flowOn(Dispatchers.IO)
                .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
                .collect { this@CategorySetViewModel.isPremium = it }
        }
    }

    fun getIconsByGroupId(groupId: CategoryGroup): List<Int> = groupedIcons[groupId]!!

    fun save(@DrawableRes categoryIcon: Int) {
        navigator?.save(categoryIcon)
    }
}

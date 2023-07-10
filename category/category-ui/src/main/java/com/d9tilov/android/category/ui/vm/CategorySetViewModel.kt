package com.d9tilov.android.category.ui.vm

import androidx.annotation.DrawableRes
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.model.CategoryGroup
import com.d9tilov.android.category.ui.navigation.CategorySetNavigator
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn

@HiltViewModel
class CategorySetViewModel @Inject constructor(
    private val billingInteractor: BillingInteractor
) : BaseViewModel<CategorySetNavigator>() {

    private val groupedIcons = mapOf(
        CategoryGroup.HOUSING to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_garbage,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_home,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_house
        ),
        CategoryGroup.TRANSPORT to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_car,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_car_repair,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_car_service,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electric_train,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_fuel,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_motorbyke,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_police,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_public_transport,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_taxi
        ),
        CategoryGroup.FOOD to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_backery,
            com.d9tilov.android.designsystem.R.drawable.ic_category_food,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_beer,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_barista,
            com.d9tilov.android.common_android.R.drawable.ic_category_cafe,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_coffee,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_coffee2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_fastfood,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_grocery,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sushi,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_water,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_turka
        ),
        CategoryGroup.UTILITIES to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_cleaning,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_garbage,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_internet,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_internet2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_wifi,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_laundry,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_laundry2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_phone,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_phone2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_phone3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_phone4,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_plumber,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_camera
        ),
        CategoryGroup.INSURANCE to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_help,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_injury,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_house_insurance,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_contract,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_two_people,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_law
        ),
        CategoryGroup.MEDICAL to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_dentist,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_doctor,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_doctor2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_doctor3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_pills,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_pills2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_pills3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_pregnant,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_heart,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_graph
        ),
        CategoryGroup.SPORT to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_boxing,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_bycicle,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_gym,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_gym2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_hike,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sports,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sports2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sports3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sports4,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sports5,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sports6,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_yoga,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_yoga2
        ),
        CategoryGroup.INVESTING to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_business,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_deal,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_investment,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_part_time_job,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_percent,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_percent2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_keys,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_salary,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sale,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sale_house,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_savings
        ),
        CategoryGroup.RECREATION to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_beach,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_home_relax,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_massage,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_massage2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_relax,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_relax2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_travels,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_trip,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_walking
        ),
        CategoryGroup.PERSONAL to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_barber_shop,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_beauty,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_clothes,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_cosmetics,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_epilation,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_hair_dryer,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_haircut,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_haircut2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_haircut_female,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_haircut_modern,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_pipette,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_pomade,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_shampoo,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_washing
        ),
        CategoryGroup.OTHERS to listOf(
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_brain,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_group,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_gift,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_gift2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_gift3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_shield,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_shield2,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_mask,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_flasher,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_settings_hand,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_password,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_key,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_scanner,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_safe,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_people_group
        ),
        CategoryGroup.UNKNOWN to listOf(
            com.d9tilov.android.designsystem.R.drawable.ic_category_food,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_grocery,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_car,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_fuel,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_clothes,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_doctor,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_internet,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_investment,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_part_time_job,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_phone3,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_public_transport,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_relax,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_salary,
            com.d9tilov.android.category_data_impl.R.drawable.ic_category_sports3
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

package com.d9tilov.moneymanager.data.base.local.db.prepopulate

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.data.base.local.db.AppDatabase.Companion.NO_ID
import com.d9tilov.moneymanager.data.base.local.db.prepopulate.entity.PrepopulateCategory
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

class PrepopulateDataManager @Inject constructor(private val context: Context) {

    fun createDefaultCategories(): List<PrepopulateCategory> {
        return listOf(
            createCategory(
                1,
                NO_ID,
                R.string.default_category_food,
                R.drawable.ic_category_food,
                R.color.category_deep_purple_a100
            ),
            createCategory(
                2,
                1,
                R.string.default_category_cafe,
                R.drawable.ic_catefory_cafe,
                R.color.blue_grey_400
            ),
            createCategory(
                3,
                NO_ID,
                R.string.default_category_car,
                R.drawable.ic_category_car,
                R.color.category_light_green_a200
            ),
            createCategory(
                4,
                NO_ID,
                R.string.default_category_doctor,
                R.drawable.ic_category_doctor,
                R.color.category_pink_300
            ),
            createCategory(
                5,
                NO_ID,
                R.string.default_category_entertainment,
                R.drawable.ic_category_entertainment,
                R.color.category_pink_a200
            ),
            createCategory(
                6,
                NO_ID,
                R.string.default_category_home,
                R.drawable.ic_category_home,
                R.color.category_teal_100
            ),
            createCategory(
                7,
                NO_ID,
                R.string.default_category_travels,
                R.drawable.ic_category_travels,
                R.color.category_yellow_400
            ),
            createCategory(
                8,
                NO_ID,
                R.string.default_category_internet,
                R.drawable.ic_category_internet,
                R.color.category_teal_600
            )
        )
    }

    private fun createCategory(
        id: Long,
        parentId: Long,
        @StringRes name: Int,
        @DrawableRes icon: Int,
        @ColorRes color: Int
    ): PrepopulateCategory {
        val bitmap = getBitmapFromVectorDrawable(icon)
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapArray: ByteArray = stream.toByteArray()
        return PrepopulateCategory(id, parentId, context.getString(name), bitmapArray, color)
    }

    private fun getBitmapFromVectorDrawable(
        drawableId: Int
    ): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        drawable?.let {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        return null
    }
}

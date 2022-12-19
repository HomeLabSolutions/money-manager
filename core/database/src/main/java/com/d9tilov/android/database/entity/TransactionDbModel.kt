package com.d9tilov.android.database.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.android.database.model.TransactionType
import java.math.BigDecimal
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "type") val type: TransactionType,
    @ColumnInfo(name = "categoryId") val categoryId: Long,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "sum") val sum: BigDecimal,
    @ColumnInfo(name = "usd_sum") val usdSum: BigDecimal,
    @ColumnInfo(name = "date") val date: LocalDateTime,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "qrCode") val qrCode: String?,
    @ColumnInfo(name = "isRegular") val isRegular: Boolean,
    @ColumnInfo(name = "inStatistics") val inStatistics: Boolean,
    @ColumnInfo(name = "location") val location: Location?,
    @ColumnInfo(name = "photo") val photoUri: String?

)

package com.d9tilov.moneymanager.regular.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.regular.domain.entity.ExecutionPeriod
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.user.data.entity.UserDbModel
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

@Entity(
    tableName = "regularTransaction",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RegularTransactionDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "type") val type: TransactionType,
    @ColumnInfo(name = "sum") val sum: BigDecimal,
    @ColumnInfo(name = "categoryId") val categoryId: Long,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "createdDate") val createdDate: LocalDateTime,
    @ColumnInfo(name = "executionPeriod") val executionPeriod: ExecutionPeriod,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "pushEnable") val pushEnable: Boolean,
    @ColumnInfo(name = "autoAdd") val autoAdd: Boolean
)

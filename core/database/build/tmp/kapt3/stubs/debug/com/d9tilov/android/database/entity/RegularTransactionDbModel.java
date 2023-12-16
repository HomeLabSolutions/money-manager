package com.d9tilov.android.database.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b#\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B]\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0005\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0012\u00a2\u0006\u0002\u0010\u0014J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0012H\u00c6\u0003J\t\u0010)\u001a\u00020\u0012H\u00c6\u0003J\t\u0010*\u001a\u00020\u0005H\u00c6\u0003J\t\u0010+\u001a\u00020\u0007H\u00c6\u0003J\t\u0010,\u001a\u00020\tH\u00c6\u0003J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0005H\u00c6\u0003J\t\u0010/\u001a\u00020\rH\u00c6\u0003J\t\u00100\u001a\u00020\u000fH\u00c6\u0003J\t\u00101\u001a\u00020\u0005H\u00c6\u0003Jw\u00102\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0012H\u00c6\u0001J\u0013\u00103\u001a\u00020\u00122\b\u00104\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00105\u001a\u000206H\u00d6\u0001J\t\u00107\u001a\u00020\u0005H\u00d6\u0001R\u0016\u0010\u0013\u001a\u00020\u00128\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0016\u0010\n\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0016\u0010\f\u001a\u00020\r8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0016\u0010\u000b\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0016\u0010\u0010\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001aR\u0016\u0010\u000e\u001a\u00020\u000f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018R\u0016\u0010\u0011\u001a\u00020\u00128\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0016R\u0016\u0010\b\u001a\u00020\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&\u00a8\u00068"}, d2 = {"Lcom/d9tilov/android/database/entity/RegularTransactionDbModel;", "", "id", "", "clientId", "", "type", "Lcom/d9tilov/android/core/model/TransactionType;", "sum", "Ljava/math/BigDecimal;", "categoryId", "currency", "createdDate", "Lkotlinx/datetime/LocalDateTime;", "executionPeriod", "Lcom/d9tilov/android/core/model/ExecutionPeriod;", "description", "pushEnable", "", "autoAdd", "(JLjava/lang/String;Lcom/d9tilov/android/core/model/TransactionType;Ljava/math/BigDecimal;JLjava/lang/String;Lkotlinx/datetime/LocalDateTime;Lcom/d9tilov/android/core/model/ExecutionPeriod;Ljava/lang/String;ZZ)V", "getAutoAdd", "()Z", "getCategoryId", "()J", "getClientId", "()Ljava/lang/String;", "getCreatedDate", "()Lkotlinx/datetime/LocalDateTime;", "getCurrency", "getDescription", "getExecutionPeriod", "()Lcom/d9tilov/android/core/model/ExecutionPeriod;", "getId", "getPushEnable", "getSum", "()Ljava/math/BigDecimal;", "getType", "()Lcom/d9tilov/android/core/model/TransactionType;", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "database_debug"})
@androidx.room.Entity(tableName = "regularTransaction", foreignKeys = {@androidx.room.ForeignKey(entity = com.d9tilov.android.database.entity.UserDbModel.class, parentColumns = {"uid"}, childColumns = {"clientId"}, onDelete = 5)})
public final class RegularTransactionDbModel {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "clientId", index = true)
    @org.jetbrains.annotations.NotNull
    private final java.lang.String clientId = null;
    @androidx.room.ColumnInfo(name = "type")
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.model.TransactionType type = null;
    @androidx.room.ColumnInfo(name = "sum")
    @org.jetbrains.annotations.NotNull
    private final java.math.BigDecimal sum = null;
    @androidx.room.ColumnInfo(name = "categoryId")
    private final long categoryId = 0L;
    @androidx.room.ColumnInfo(name = "currency")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String currency = null;
    @androidx.room.ColumnInfo(name = "createdDate")
    @org.jetbrains.annotations.NotNull
    private final kotlinx.datetime.LocalDateTime createdDate = null;
    @androidx.room.ColumnInfo(name = "executionPeriod")
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.model.ExecutionPeriod executionPeriod = null;
    @androidx.room.ColumnInfo(name = "description")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    @androidx.room.ColumnInfo(name = "pushEnable")
    private final boolean pushEnable = false;
    @androidx.room.ColumnInfo(name = "autoAdd")
    private final boolean autoAdd = false;
    
    public RegularTransactionDbModel(long id, @org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType type, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal sum, long categoryId, @org.jetbrains.annotations.NotNull
    java.lang.String currency, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime createdDate, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.ExecutionPeriod executionPeriod, @org.jetbrains.annotations.NotNull
    java.lang.String description, boolean pushEnable, boolean autoAdd) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getClientId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.core.model.TransactionType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal getSum() {
        return null;
    }
    
    public final long getCategoryId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrency() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.datetime.LocalDateTime getCreatedDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.core.model.ExecutionPeriod getExecutionPeriod() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final boolean getPushEnable() {
        return false;
    }
    
    public final boolean getAutoAdd() {
        return false;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.core.model.TransactionType component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal component4() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.datetime.LocalDateTime component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.core.model.ExecutionPeriod component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.database.entity.RegularTransactionDbModel copy(long id, @org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType type, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal sum, long categoryId, @org.jetbrains.annotations.NotNull
    java.lang.String currency, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime createdDate, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.ExecutionPeriod executionPeriod, @org.jetbrains.annotations.NotNull
    java.lang.String description, boolean pushEnable, boolean autoAdd) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}
package com.d9tilov.android.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&J\b\u0010\u000f\u001a\u00020\u0010H&J\b\u0010\u0011\u001a\u00020\u0012H&\u00a8\u0006\u0014"}, d2 = {"Lcom/d9tilov/android/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "budgetDao", "Lcom/d9tilov/android/database/dao/BudgetDao;", "categoryDao", "Lcom/d9tilov/android/database/dao/CategoryDao;", "currencyDao", "Lcom/d9tilov/android/database/dao/CurrencyListDao;", "goalDao", "Lcom/d9tilov/android/database/dao/GoalDao;", "mainCurrencyDao", "Lcom/d9tilov/android/database/dao/MainCurrencyDao;", "regularTransactionDao", "Lcom/d9tilov/android/database/dao/RegularTransactionDao;", "transactionDao", "Lcom/d9tilov/android/database/dao/TransactionDao;", "userDao", "Lcom/d9tilov/android/database/dao/UserDao;", "Companion", "database_debug"})
@androidx.room.Database(entities = {com.d9tilov.android.database.entity.UserDbModel.class, com.d9tilov.android.database.entity.CategoryDbModel.class, com.d9tilov.android.database.entity.TransactionDbModel.class, com.d9tilov.android.database.entity.BudgetDbModel.class, com.d9tilov.android.database.entity.CurrencyDbModel.class, com.d9tilov.android.database.entity.MainCurrencyDbModel.class, com.d9tilov.android.database.entity.RegularTransactionDbModel.class, com.d9tilov.android.database.entity.GoalDbModel.class}, version = 1, exportSchema = true)
@androidx.room.TypeConverters(value = {com.d9tilov.android.database.converters.CurrencyConverter.class, com.d9tilov.android.database.converters.DateConverter.class, com.d9tilov.android.database.converters.ModelTypeConverter.class})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    public static final int VERSION_NUMBER = 1;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.database.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.CategoryDao categoryDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.TransactionDao transactionDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.BudgetDao budgetDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.CurrencyListDao currencyDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.MainCurrencyDao mainCurrencyDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.RegularTransactionDao regularTransactionDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.dao.GoalDao goalDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/d9tilov/android/database/AppDatabase$Companion;", "", "()V", "VERSION_NUMBER", "", "database_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
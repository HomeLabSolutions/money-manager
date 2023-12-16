package com.d9tilov.android.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.d9tilov.android.database.dao.BudgetDao;
import com.d9tilov.android.database.dao.BudgetDao_Impl;
import com.d9tilov.android.database.dao.CategoryDao;
import com.d9tilov.android.database.dao.CategoryDao_Impl;
import com.d9tilov.android.database.dao.CurrencyListDao;
import com.d9tilov.android.database.dao.CurrencyListDao_Impl;
import com.d9tilov.android.database.dao.GoalDao;
import com.d9tilov.android.database.dao.GoalDao_Impl;
import com.d9tilov.android.database.dao.MainCurrencyDao;
import com.d9tilov.android.database.dao.MainCurrencyDao_Impl;
import com.d9tilov.android.database.dao.RegularTransactionDao;
import com.d9tilov.android.database.dao.RegularTransactionDao_Impl;
import com.d9tilov.android.database.dao.TransactionDao;
import com.d9tilov.android.database.dao.TransactionDao_Impl;
import com.d9tilov.android.database.dao.UserDao;
import com.d9tilov.android.database.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile CategoryDao _categoryDao;

  private volatile TransactionDao _transactionDao;

  private volatile BudgetDao _budgetDao;

  private volatile CurrencyListDao _currencyListDao;

  private volatile MainCurrencyDao _mainCurrencyDao;

  private volatile RegularTransactionDao _regularTransactionDao;

  private volatile GoalDao _goalDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`uid` TEXT NOT NULL, `firstName` TEXT, `lastName` TEXT, `currentCurrencyCode` TEXT NOT NULL, `showPrepopulate` INTEGER NOT NULL, `fiscalDay` INTEGER NOT NULL, PRIMARY KEY(`uid`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` TEXT NOT NULL, `parentId` INTEGER NOT NULL, `type` INTEGER NOT NULL, `name` TEXT NOT NULL, `icon` INTEGER NOT NULL, `color` INTEGER NOT NULL, `usageCount` INTEGER NOT NULL, FOREIGN KEY(`clientId`) REFERENCES `users`(`uid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_clientId` ON `categories` (`clientId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` TEXT NOT NULL, `type` INTEGER NOT NULL, `categoryId` INTEGER NOT NULL, `currency` TEXT NOT NULL, `sum` TEXT NOT NULL, `usd_sum` TEXT NOT NULL, `date` INTEGER NOT NULL, `description` TEXT NOT NULL, `qrCode` TEXT, `isRegular` INTEGER NOT NULL, `inStatistics` INTEGER NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `photo` TEXT, FOREIGN KEY(`clientId`) REFERENCES `users`(`uid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_clientId` ON `transactions` (`clientId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budget` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` TEXT NOT NULL, `currency` TEXT NOT NULL, `sum` TEXT NOT NULL, `saveSum` TEXT NOT NULL, `createdDate` INTEGER NOT NULL, FOREIGN KEY(`clientId`) REFERENCES `users`(`uid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_budget_clientId` ON `budget` (`clientId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `currency` (`id` TEXT NOT NULL, `symbol` TEXT NOT NULL, `value` TEXT NOT NULL, `lastUpdateTime` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `main_currency` (`id` INTEGER NOT NULL, `clientId` TEXT NOT NULL, `code` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`clientId`) REFERENCES `users`(`uid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_main_currency_clientId` ON `main_currency` (`clientId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `regularTransaction` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` TEXT NOT NULL, `type` INTEGER NOT NULL, `sum` TEXT NOT NULL, `categoryId` INTEGER NOT NULL, `currency` TEXT NOT NULL, `createdDate` INTEGER NOT NULL, `executionPeriod` TEXT NOT NULL, `description` TEXT NOT NULL, `pushEnable` INTEGER NOT NULL, `autoAdd` INTEGER NOT NULL, FOREIGN KEY(`clientId`) REFERENCES `users`(`uid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_regularTransaction_clientId` ON `regularTransaction` (`clientId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `goal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` TEXT NOT NULL, `currency` TEXT NOT NULL, `name` TEXT NOT NULL, `targetSum` TEXT NOT NULL, `createdDate` INTEGER NOT NULL, `description` TEXT NOT NULL, FOREIGN KEY(`clientId`) REFERENCES `users`(`uid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_goal_clientId` ON `goal` (`clientId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '147939a71a17778ec482d7394cc25873')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `budget`");
        db.execSQL("DROP TABLE IF EXISTS `currency`");
        db.execSQL("DROP TABLE IF EXISTS `main_currency`");
        db.execSQL("DROP TABLE IF EXISTS `regularTransaction`");
        db.execSQL("DROP TABLE IF EXISTS `goal`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("uid", new TableInfo.Column("uid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("firstName", new TableInfo.Column("firstName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("lastName", new TableInfo.Column("lastName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("currentCurrencyCode", new TableInfo.Column("currentCurrencyCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("showPrepopulate", new TableInfo.Column("showPrepopulate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fiscalDay", new TableInfo.Column("fiscalDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.d9tilov.android.database.entity.UserDbModel).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(8);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("clientId", new TableInfo.Column("clientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("parentId", new TableInfo.Column("parentId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("type", new TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("icon", new TableInfo.Column("icon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("color", new TableInfo.Column("color", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("usageCount", new TableInfo.Column("usageCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCategories.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("uid")));
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(1);
        _indicesCategories.add(new TableInfo.Index("index_categories_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.d9tilov.android.database.entity.CategoryDbModel).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(15);
        _columnsTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("clientId", new TableInfo.Column("clientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("type", new TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("sum", new TableInfo.Column("sum", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("usd_sum", new TableInfo.Column("usd_sum", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("qrCode", new TableInfo.Column("qrCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("isRegular", new TableInfo.Column("isRegular", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("inStatistics", new TableInfo.Column("inStatistics", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("photo", new TableInfo.Column("photo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("uid")));
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(1);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.d9tilov.android.database.entity.TransactionDbModel).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsBudget = new HashMap<String, TableInfo.Column>(6);
        _columnsBudget.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudget.put("clientId", new TableInfo.Column("clientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudget.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudget.put("sum", new TableInfo.Column("sum", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudget.put("saveSum", new TableInfo.Column("saveSum", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudget.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudget = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBudget.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("uid")));
        final HashSet<TableInfo.Index> _indicesBudget = new HashSet<TableInfo.Index>(1);
        _indicesBudget.add(new TableInfo.Index("index_budget_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        final TableInfo _infoBudget = new TableInfo("budget", _columnsBudget, _foreignKeysBudget, _indicesBudget);
        final TableInfo _existingBudget = TableInfo.read(db, "budget");
        if (!_infoBudget.equals(_existingBudget)) {
          return new RoomOpenHelper.ValidationResult(false, "budget(com.d9tilov.android.database.entity.BudgetDbModel).\n"
                  + " Expected:\n" + _infoBudget + "\n"
                  + " Found:\n" + _existingBudget);
        }
        final HashMap<String, TableInfo.Column> _columnsCurrency = new HashMap<String, TableInfo.Column>(4);
        _columnsCurrency.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrency.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrency.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrency.put("lastUpdateTime", new TableInfo.Column("lastUpdateTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCurrency = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCurrency = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCurrency = new TableInfo("currency", _columnsCurrency, _foreignKeysCurrency, _indicesCurrency);
        final TableInfo _existingCurrency = TableInfo.read(db, "currency");
        if (!_infoCurrency.equals(_existingCurrency)) {
          return new RoomOpenHelper.ValidationResult(false, "currency(com.d9tilov.android.database.entity.CurrencyDbModel).\n"
                  + " Expected:\n" + _infoCurrency + "\n"
                  + " Found:\n" + _existingCurrency);
        }
        final HashMap<String, TableInfo.Column> _columnsMainCurrency = new HashMap<String, TableInfo.Column>(3);
        _columnsMainCurrency.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMainCurrency.put("clientId", new TableInfo.Column("clientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMainCurrency.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMainCurrency = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMainCurrency.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("uid")));
        final HashSet<TableInfo.Index> _indicesMainCurrency = new HashSet<TableInfo.Index>(1);
        _indicesMainCurrency.add(new TableInfo.Index("index_main_currency_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        final TableInfo _infoMainCurrency = new TableInfo("main_currency", _columnsMainCurrency, _foreignKeysMainCurrency, _indicesMainCurrency);
        final TableInfo _existingMainCurrency = TableInfo.read(db, "main_currency");
        if (!_infoMainCurrency.equals(_existingMainCurrency)) {
          return new RoomOpenHelper.ValidationResult(false, "main_currency(com.d9tilov.android.database.entity.MainCurrencyDbModel).\n"
                  + " Expected:\n" + _infoMainCurrency + "\n"
                  + " Found:\n" + _existingMainCurrency);
        }
        final HashMap<String, TableInfo.Column> _columnsRegularTransaction = new HashMap<String, TableInfo.Column>(11);
        _columnsRegularTransaction.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("clientId", new TableInfo.Column("clientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("type", new TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("sum", new TableInfo.Column("sum", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("executionPeriod", new TableInfo.Column("executionPeriod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("pushEnable", new TableInfo.Column("pushEnable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegularTransaction.put("autoAdd", new TableInfo.Column("autoAdd", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRegularTransaction = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRegularTransaction.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("uid")));
        final HashSet<TableInfo.Index> _indicesRegularTransaction = new HashSet<TableInfo.Index>(1);
        _indicesRegularTransaction.add(new TableInfo.Index("index_regularTransaction_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        final TableInfo _infoRegularTransaction = new TableInfo("regularTransaction", _columnsRegularTransaction, _foreignKeysRegularTransaction, _indicesRegularTransaction);
        final TableInfo _existingRegularTransaction = TableInfo.read(db, "regularTransaction");
        if (!_infoRegularTransaction.equals(_existingRegularTransaction)) {
          return new RoomOpenHelper.ValidationResult(false, "regularTransaction(com.d9tilov.android.database.entity.RegularTransactionDbModel).\n"
                  + " Expected:\n" + _infoRegularTransaction + "\n"
                  + " Found:\n" + _existingRegularTransaction);
        }
        final HashMap<String, TableInfo.Column> _columnsGoal = new HashMap<String, TableInfo.Column>(7);
        _columnsGoal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoal.put("clientId", new TableInfo.Column("clientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoal.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoal.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoal.put("targetSum", new TableInfo.Column("targetSum", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoal.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoal.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGoal = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysGoal.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("uid")));
        final HashSet<TableInfo.Index> _indicesGoal = new HashSet<TableInfo.Index>(1);
        _indicesGoal.add(new TableInfo.Index("index_goal_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        final TableInfo _infoGoal = new TableInfo("goal", _columnsGoal, _foreignKeysGoal, _indicesGoal);
        final TableInfo _existingGoal = TableInfo.read(db, "goal");
        if (!_infoGoal.equals(_existingGoal)) {
          return new RoomOpenHelper.ValidationResult(false, "goal(com.d9tilov.android.database.entity.GoalDbModel).\n"
                  + " Expected:\n" + _infoGoal + "\n"
                  + " Found:\n" + _existingGoal);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "147939a71a17778ec482d7394cc25873", "bf6732687e73dd1fd2d83c0f6104abcc");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","categories","transactions","budget","currency","main_currency","regularTransaction","goal");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `budget`");
      _db.execSQL("DELETE FROM `currency`");
      _db.execSQL("DELETE FROM `main_currency`");
      _db.execSQL("DELETE FROM `regularTransaction`");
      _db.execSQL("DELETE FROM `goal`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetDao.class, BudgetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CurrencyListDao.class, CurrencyListDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MainCurrencyDao.class, MainCurrencyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RegularTransactionDao.class, RegularTransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GoalDao.class, GoalDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }

  @Override
  public BudgetDao budgetDao() {
    if (_budgetDao != null) {
      return _budgetDao;
    } else {
      synchronized(this) {
        if(_budgetDao == null) {
          _budgetDao = new BudgetDao_Impl(this);
        }
        return _budgetDao;
      }
    }
  }

  @Override
  public CurrencyListDao currencyDao() {
    if (_currencyListDao != null) {
      return _currencyListDao;
    } else {
      synchronized(this) {
        if(_currencyListDao == null) {
          _currencyListDao = new CurrencyListDao_Impl(this);
        }
        return _currencyListDao;
      }
    }
  }

  @Override
  public MainCurrencyDao mainCurrencyDao() {
    if (_mainCurrencyDao != null) {
      return _mainCurrencyDao;
    } else {
      synchronized(this) {
        if(_mainCurrencyDao == null) {
          _mainCurrencyDao = new MainCurrencyDao_Impl(this);
        }
        return _mainCurrencyDao;
      }
    }
  }

  @Override
  public RegularTransactionDao regularTransactionDao() {
    if (_regularTransactionDao != null) {
      return _regularTransactionDao;
    } else {
      synchronized(this) {
        if(_regularTransactionDao == null) {
          _regularTransactionDao = new RegularTransactionDao_Impl(this);
        }
        return _regularTransactionDao;
      }
    }
  }

  @Override
  public GoalDao goalDao() {
    if (_goalDao != null) {
      return _goalDao;
    } else {
      synchronized(this) {
        if(_goalDao == null) {
          _goalDao = new GoalDao_Impl(this);
        }
        return _goalDao;
      }
    }
  }
}

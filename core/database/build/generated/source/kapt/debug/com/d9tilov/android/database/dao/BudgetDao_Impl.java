package com.d9tilov.android.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.d9tilov.android.database.converters.CurrencyConverter;
import com.d9tilov.android.database.converters.DateConverter;
import com.d9tilov.android.database.entity.BudgetDbModel;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.LocalDateTime;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BudgetDbModel> __insertionAdapterOfBudgetDbModel;

  private final EntityDeletionOrUpdateAdapter<BudgetDbModel> __deletionAdapterOfBudgetDbModel;

  private final EntityDeletionOrUpdateAdapter<BudgetDbModel> __updateAdapterOfBudgetDbModel;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetDbModel = new EntityInsertionAdapter<BudgetDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budget` (`id`,`clientId`,`currency`,`sum`,`saveSum`,`createdDate`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getClientId());
        }
        if (entity.getCurrency() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCurrency());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSum());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSaveSum());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final long _tmp_2 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getCreatedDate());
        statement.bindLong(6, _tmp_2);
      }
    };
    this.__deletionAdapterOfBudgetDbModel = new EntityDeletionOrUpdateAdapter<BudgetDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `budget` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetDbModel entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBudgetDbModel = new EntityDeletionOrUpdateAdapter<BudgetDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `budget` SET `id` = ?,`clientId` = ?,`currency` = ?,`sum` = ?,`saveSum` = ?,`createdDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getClientId());
        }
        if (entity.getCurrency() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCurrency());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSum());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSaveSum());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final long _tmp_2 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getCreatedDate());
        statement.bindLong(6, _tmp_2);
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final BudgetDbModel budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBudgetDbModel.insert(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BudgetDbModel budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBudgetDbModel.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BudgetDbModel budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBudgetDbModel.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BudgetDbModel> get(final String uid) {
    final String _sql = "SELECT * FROM budget WHERE clientId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budget"}, new Callable<BudgetDbModel>() {
      @Override
      @Nullable
      public BudgetDbModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfSaveSum = CursorUtil.getColumnIndexOrThrow(_cursor, "saveSum");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final BudgetDbModel _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final String _tmpCurrency;
            if (_cursor.isNull(_cursorIndexOfCurrency)) {
              _tmpCurrency = null;
            } else {
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            }
            final BigDecimal _tmpSum;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfSum)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfSum);
            }
            _tmpSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp);
            final BigDecimal _tmpSaveSum;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSaveSum)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSaveSum);
            }
            _tmpSaveSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_1);
            final LocalDateTime _tmpCreatedDate;
            final long _tmp_2;
            _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedDate);
            _tmpCreatedDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_2);
            _result = new BudgetDbModel(_tmpId,_tmpClientId,_tmpCurrency,_tmpSum,_tmpSaveSum,_tmpCreatedDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

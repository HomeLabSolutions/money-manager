package com.d9tilov.android.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.d9tilov.android.core.model.ExecutionPeriod;
import com.d9tilov.android.core.model.TransactionType;
import com.d9tilov.android.database.converters.CurrencyConverter;
import com.d9tilov.android.database.converters.DateConverter;
import com.d9tilov.android.database.converters.ModelTypeConverter;
import com.d9tilov.android.database.entity.RegularTransactionDbModel;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public final class RegularTransactionDao_Impl implements RegularTransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RegularTransactionDbModel> __insertionAdapterOfRegularTransactionDbModel;

  private final EntityDeletionOrUpdateAdapter<RegularTransactionDbModel> __deletionAdapterOfRegularTransactionDbModel;

  private final EntityDeletionOrUpdateAdapter<RegularTransactionDbModel> __updateAdapterOfRegularTransactionDbModel;

  public RegularTransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRegularTransactionDbModel = new EntityInsertionAdapter<RegularTransactionDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `regularTransaction` (`id`,`clientId`,`type`,`sum`,`categoryId`,`currency`,`createdDate`,`executionPeriod`,`description`,`pushEnable`,`autoAdd`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegularTransactionDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getClientId());
        }
        final int _tmp = ModelTypeConverter.INSTANCE.fromTransactionType(entity.getType());
        statement.bindLong(3, _tmp);
        final String _tmp_1 = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSum());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        statement.bindLong(5, entity.getCategoryId());
        if (entity.getCurrency() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCurrency());
        }
        final long _tmp_2 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getCreatedDate());
        statement.bindLong(7, _tmp_2);
        final String _tmp_3 = ModelTypeConverter.INSTANCE.fromExecutionPeriod(entity.getExecutionPeriod());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_3);
        }
        if (entity.getDescription() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDescription());
        }
        final int _tmp_4 = entity.getPushEnable() ? 1 : 0;
        statement.bindLong(10, _tmp_4);
        final int _tmp_5 = entity.getAutoAdd() ? 1 : 0;
        statement.bindLong(11, _tmp_5);
      }
    };
    this.__deletionAdapterOfRegularTransactionDbModel = new EntityDeletionOrUpdateAdapter<RegularTransactionDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `regularTransaction` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegularTransactionDbModel entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRegularTransactionDbModel = new EntityDeletionOrUpdateAdapter<RegularTransactionDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `regularTransaction` SET `id` = ?,`clientId` = ?,`type` = ?,`sum` = ?,`categoryId` = ?,`currency` = ?,`createdDate` = ?,`executionPeriod` = ?,`description` = ?,`pushEnable` = ?,`autoAdd` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegularTransactionDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getClientId());
        }
        final int _tmp = ModelTypeConverter.INSTANCE.fromTransactionType(entity.getType());
        statement.bindLong(3, _tmp);
        final String _tmp_1 = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSum());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        statement.bindLong(5, entity.getCategoryId());
        if (entity.getCurrency() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCurrency());
        }
        final long _tmp_2 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getCreatedDate());
        statement.bindLong(7, _tmp_2);
        final String _tmp_3 = ModelTypeConverter.INSTANCE.fromExecutionPeriod(entity.getExecutionPeriod());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_3);
        }
        if (entity.getDescription() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDescription());
        }
        final int _tmp_4 = entity.getPushEnable() ? 1 : 0;
        statement.bindLong(10, _tmp_4);
        final int _tmp_5 = entity.getAutoAdd() ? 1 : 0;
        statement.bindLong(11, _tmp_5);
        statement.bindLong(12, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final RegularTransactionDbModel budget,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRegularTransactionDbModel.insert(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RegularTransactionDbModel budget,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRegularTransactionDbModel.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RegularTransactionDbModel budget,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRegularTransactionDbModel.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RegularTransactionDbModel>> getAll(final String uid,
      final TransactionType transactionType) {
    final String _sql = "SELECT * FROM regularTransaction WHERE clientId=? AND type=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    final int _tmp = ModelTypeConverter.INSTANCE.fromTransactionType(transactionType);
    _statement.bindLong(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"regularTransaction"}, new Callable<List<RegularTransactionDbModel>>() {
      @Override
      @NonNull
      public List<RegularTransactionDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfExecutionPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "executionPeriod");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPushEnable = CursorUtil.getColumnIndexOrThrow(_cursor, "pushEnable");
          final int _cursorIndexOfAutoAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "autoAdd");
          final List<RegularTransactionDbModel> _result = new ArrayList<RegularTransactionDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RegularTransactionDbModel _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final TransactionType _tmpType;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfType);
            _tmpType = ModelTypeConverter.INSTANCE.toTransactionType(_tmp_1);
            final BigDecimal _tmpSum;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfSum)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfSum);
            }
            _tmpSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_2);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpCurrency;
            if (_cursor.isNull(_cursorIndexOfCurrency)) {
              _tmpCurrency = null;
            } else {
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            }
            final LocalDateTime _tmpCreatedDate;
            final long _tmp_3;
            _tmp_3 = _cursor.getLong(_cursorIndexOfCreatedDate);
            _tmpCreatedDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_3);
            final ExecutionPeriod _tmpExecutionPeriod;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfExecutionPeriod)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfExecutionPeriod);
            }
            _tmpExecutionPeriod = ModelTypeConverter.INSTANCE.toExecutionPeriod(_tmp_4);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpPushEnable;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfPushEnable);
            _tmpPushEnable = _tmp_5 != 0;
            final boolean _tmpAutoAdd;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfAutoAdd);
            _tmpAutoAdd = _tmp_6 != 0;
            _item = new RegularTransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpSum,_tmpCategoryId,_tmpCurrency,_tmpCreatedDate,_tmpExecutionPeriod,_tmpDescription,_tmpPushEnable,_tmpAutoAdd);
            _result.add(_item);
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

  @Override
  public Object getById(final String uid, final long id,
      final Continuation<? super RegularTransactionDbModel> $completion) {
    final String _sql = "SELECT * FROM regularTransaction WHERE clientId=? AND id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RegularTransactionDbModel>() {
      @Override
      @NonNull
      public RegularTransactionDbModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfExecutionPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "executionPeriod");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPushEnable = CursorUtil.getColumnIndexOrThrow(_cursor, "pushEnable");
          final int _cursorIndexOfAutoAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "autoAdd");
          final RegularTransactionDbModel _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final TransactionType _tmpType;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfType);
            _tmpType = ModelTypeConverter.INSTANCE.toTransactionType(_tmp);
            final BigDecimal _tmpSum;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSum)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSum);
            }
            _tmpSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_1);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpCurrency;
            if (_cursor.isNull(_cursorIndexOfCurrency)) {
              _tmpCurrency = null;
            } else {
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            }
            final LocalDateTime _tmpCreatedDate;
            final long _tmp_2;
            _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedDate);
            _tmpCreatedDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_2);
            final ExecutionPeriod _tmpExecutionPeriod;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExecutionPeriod)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfExecutionPeriod);
            }
            _tmpExecutionPeriod = ModelTypeConverter.INSTANCE.toExecutionPeriod(_tmp_3);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpPushEnable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfPushEnable);
            _tmpPushEnable = _tmp_4 != 0;
            final boolean _tmpAutoAdd;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfAutoAdd);
            _tmpAutoAdd = _tmp_5 != 0;
            _result = new RegularTransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpSum,_tmpCategoryId,_tmpCurrency,_tmpCreatedDate,_tmpExecutionPeriod,_tmpDescription,_tmpPushEnable,_tmpAutoAdd);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByCategoryId(final String uid, final long categoryId,
      final Continuation<? super List<RegularTransactionDbModel>> $completion) {
    final String _sql = "SELECT * FROM regularTransaction WHERE clientId =? AND categoryId =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, categoryId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RegularTransactionDbModel>>() {
      @Override
      @NonNull
      public List<RegularTransactionDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfExecutionPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "executionPeriod");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPushEnable = CursorUtil.getColumnIndexOrThrow(_cursor, "pushEnable");
          final int _cursorIndexOfAutoAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "autoAdd");
          final List<RegularTransactionDbModel> _result = new ArrayList<RegularTransactionDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RegularTransactionDbModel _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final TransactionType _tmpType;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfType);
            _tmpType = ModelTypeConverter.INSTANCE.toTransactionType(_tmp);
            final BigDecimal _tmpSum;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSum)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSum);
            }
            _tmpSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_1);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpCurrency;
            if (_cursor.isNull(_cursorIndexOfCurrency)) {
              _tmpCurrency = null;
            } else {
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            }
            final LocalDateTime _tmpCreatedDate;
            final long _tmp_2;
            _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedDate);
            _tmpCreatedDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_2);
            final ExecutionPeriod _tmpExecutionPeriod;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExecutionPeriod)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfExecutionPeriod);
            }
            _tmpExecutionPeriod = ModelTypeConverter.INSTANCE.toExecutionPeriod(_tmp_3);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final boolean _tmpPushEnable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfPushEnable);
            _tmpPushEnable = _tmp_4 != 0;
            final boolean _tmpAutoAdd;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfAutoAdd);
            _tmpAutoAdd = _tmp_5 != 0;
            _item = new RegularTransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpSum,_tmpCategoryId,_tmpCurrency,_tmpCreatedDate,_tmpExecutionPeriod,_tmpDescription,_tmpPushEnable,_tmpAutoAdd);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

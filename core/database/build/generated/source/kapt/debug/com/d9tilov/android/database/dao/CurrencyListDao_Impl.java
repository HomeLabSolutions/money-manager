package com.d9tilov.android.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.d9tilov.android.database.converters.CurrencyConverter;
import com.d9tilov.android.database.entity.CurrencyDbModel;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CurrencyListDao_Impl implements CurrencyListDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CurrencyDbModel> __insertionAdapterOfCurrencyDbModel;

  private final EntityDeletionOrUpdateAdapter<CurrencyDbModel> __updateAdapterOfCurrencyDbModel;

  public CurrencyListDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCurrencyDbModel = new EntityInsertionAdapter<CurrencyDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `currency` (`id`,`symbol`,`value`,`lastUpdateTime`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CurrencyDbModel entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getSymbol() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSymbol());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getValue());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        statement.bindLong(4, entity.getLastUpdateTime());
      }
    };
    this.__updateAdapterOfCurrencyDbModel = new EntityDeletionOrUpdateAdapter<CurrencyDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `currency` SET `id` = ?,`symbol` = ?,`value` = ?,`lastUpdateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CurrencyDbModel entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getSymbol() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSymbol());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getValue());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        statement.bindLong(4, entity.getLastUpdateTime());
        if (entity.getId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getId());
        }
      }
    };
  }

  @Override
  public Object insert(final List<CurrencyDbModel> list, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCurrencyDbModel.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object update(final CurrencyDbModel currency, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCurrencyDbModel.handle(currency);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<CurrencyDbModel>> getAll() {
    final String _sql = "SELECT * FROM currency";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"currency"}, new Callable<List<CurrencyDbModel>>() {
      @Override
      @NonNull
      public List<CurrencyDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfLastUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdateTime");
          final List<CurrencyDbModel> _result = new ArrayList<CurrencyDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CurrencyDbModel _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpSymbol;
            if (_cursor.isNull(_cursorIndexOfSymbol)) {
              _tmpSymbol = null;
            } else {
              _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            }
            final BigDecimal _tmpValue;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfValue);
            }
            _tmpValue = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp);
            final long _tmpLastUpdateTime;
            _tmpLastUpdateTime = _cursor.getLong(_cursorIndexOfLastUpdateTime);
            _item = new CurrencyDbModel(_tmpId,_tmpSymbol,_tmpValue,_tmpLastUpdateTime);
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
  public CurrencyDbModel getByCode(final String code) {
    final String _sql = "SELECT * FROM currency WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (code == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, code);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfLastUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdateTime");
      final CurrencyDbModel _result;
      if (_cursor.moveToFirst()) {
        final String _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getString(_cursorIndexOfId);
        }
        final String _tmpSymbol;
        if (_cursor.isNull(_cursorIndexOfSymbol)) {
          _tmpSymbol = null;
        } else {
          _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        }
        final BigDecimal _tmpValue;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfValue)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfValue);
        }
        _tmpValue = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp);
        final long _tmpLastUpdateTime;
        _tmpLastUpdateTime = _cursor.getLong(_cursorIndexOfLastUpdateTime);
        _result = new CurrencyDbModel(_tmpId,_tmpSymbol,_tmpValue,_tmpLastUpdateTime);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public long getLastUpdateTime(final String code) {
    final String _sql = "SELECT lastUpdateTime FROM currency WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (code == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, code);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final long _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getLong(0);
      } else {
        _result = 0L;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

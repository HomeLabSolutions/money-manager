package com.d9tilov.android.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.d9tilov.android.database.converters.CurrencyConverter;
import com.d9tilov.android.database.converters.DateConverter;
import com.d9tilov.android.database.entity.TransactionDbModel;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionDbModel> __insertionAdapterOfTransactionDbModel;

  private final EntityDeletionOrUpdateAdapter<TransactionDbModel> __updateAdapterOfTransactionDbModel;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByCategoryId;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionDbModel = new EntityInsertionAdapter<TransactionDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transactions` (`id`,`clientId`,`type`,`categoryId`,`currency`,`sum`,`usd_sum`,`date`,`description`,`qrCode`,`isRegular`,`inStatistics`,`latitude`,`longitude`,`photo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getClientId());
        }
        statement.bindLong(3, entity.getType());
        statement.bindLong(4, entity.getCategoryId());
        if (entity.getCurrency() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCurrency());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSum());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp);
        }
        final String _tmp_1 = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getUsdSum());
        if (_tmp_1 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_1);
        }
        final long _tmp_2 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getDate());
        statement.bindLong(8, _tmp_2);
        if (entity.getDescription() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDescription());
        }
        if (entity.getQrCode() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getQrCode());
        }
        final int _tmp_3 = entity.isRegular() ? 1 : 0;
        statement.bindLong(11, _tmp_3);
        final int _tmp_4 = entity.getInStatistics() ? 1 : 0;
        statement.bindLong(12, _tmp_4);
        statement.bindDouble(13, entity.getLatitude());
        statement.bindDouble(14, entity.getLongitude());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getPhotoUri());
        }
      }
    };
    this.__updateAdapterOfTransactionDbModel = new EntityDeletionOrUpdateAdapter<TransactionDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `id` = ?,`clientId` = ?,`type` = ?,`categoryId` = ?,`currency` = ?,`sum` = ?,`usd_sum` = ?,`date` = ?,`description` = ?,`qrCode` = ?,`isRegular` = ?,`inStatistics` = ?,`latitude` = ?,`longitude` = ?,`photo` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getClientId());
        }
        statement.bindLong(3, entity.getType());
        statement.bindLong(4, entity.getCategoryId());
        if (entity.getCurrency() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCurrency());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getSum());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp);
        }
        final String _tmp_1 = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getUsdSum());
        if (_tmp_1 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_1);
        }
        final long _tmp_2 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getDate());
        statement.bindLong(8, _tmp_2);
        if (entity.getDescription() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDescription());
        }
        if (entity.getQrCode() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getQrCode());
        }
        final int _tmp_3 = entity.isRegular() ? 1 : 0;
        statement.bindLong(11, _tmp_3);
        final int _tmp_4 = entity.getInStatistics() ? 1 : 0;
        statement.bindLong(12, _tmp_4);
        statement.bindDouble(13, entity.getLatitude());
        statement.bindDouble(14, entity.getLongitude());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getPhotoUri());
        }
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions WHERE clientId=? AND id=?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByCategoryId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions WHERE rowId in(SELECT rowId from transactions WHERE clientId=? AND categoryId=? LIMIT 1)";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions WHERE clientId=?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TransactionDbModel transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionDbModel.insert(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final TransactionDbModel transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransactionDbModel.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final String clientId, final long id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        if (clientId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, clientId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByCategoryId(final String clientId, final long categoryId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByCategoryId.acquire();
        int _argIndex = 1;
        if (clientId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, clientId);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, categoryId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByCategoryId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final String clientId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
        int _argIndex = 1;
        if (clientId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, clientId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public PagingSource<Integer, TransactionDbModel> getAllByType(final String clientId,
      final LocalDateTime from, final LocalDateTime to, final int type) {
    final String _sql = "SELECT * FROM transactions WHERE clientId=? AND type = ? AND date >= ? AND date <= ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (clientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, clientId);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, type);
    _argIndex = 3;
    final long _tmp = DateConverter.INSTANCE.fromOffsetDateTime(from);
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 4;
    final long _tmp_1 = DateConverter.INSTANCE.fromOffsetDateTime(to);
    _statement.bindLong(_argIndex, _tmp_1);
    return new LimitOffsetPagingSource<TransactionDbModel>(_statement, __db, "transactions") {
      @Override
      @NonNull
      protected List<TransactionDbModel> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(cursor, "clientId");
        final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(cursor, "type");
        final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(cursor, "categoryId");
        final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(cursor, "currency");
        final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(cursor, "sum");
        final int _cursorIndexOfUsdSum = CursorUtil.getColumnIndexOrThrow(cursor, "usd_sum");
        final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(cursor, "date");
        final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(cursor, "description");
        final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(cursor, "qrCode");
        final int _cursorIndexOfIsRegular = CursorUtil.getColumnIndexOrThrow(cursor, "isRegular");
        final int _cursorIndexOfInStatistics = CursorUtil.getColumnIndexOrThrow(cursor, "inStatistics");
        final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(cursor, "latitude");
        final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(cursor, "longitude");
        final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(cursor, "photo");
        final List<TransactionDbModel> _result = new ArrayList<TransactionDbModel>(cursor.getCount());
        while (cursor.moveToNext()) {
          final TransactionDbModel _item;
          final long _tmpId;
          _tmpId = cursor.getLong(_cursorIndexOfId);
          final String _tmpClientId;
          if (cursor.isNull(_cursorIndexOfClientId)) {
            _tmpClientId = null;
          } else {
            _tmpClientId = cursor.getString(_cursorIndexOfClientId);
          }
          final int _tmpType;
          _tmpType = cursor.getInt(_cursorIndexOfType);
          final long _tmpCategoryId;
          _tmpCategoryId = cursor.getLong(_cursorIndexOfCategoryId);
          final String _tmpCurrency;
          if (cursor.isNull(_cursorIndexOfCurrency)) {
            _tmpCurrency = null;
          } else {
            _tmpCurrency = cursor.getString(_cursorIndexOfCurrency);
          }
          final BigDecimal _tmpSum;
          final String _tmp_2;
          if (cursor.isNull(_cursorIndexOfSum)) {
            _tmp_2 = null;
          } else {
            _tmp_2 = cursor.getString(_cursorIndexOfSum);
          }
          _tmpSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_2);
          final BigDecimal _tmpUsdSum;
          final String _tmp_3;
          if (cursor.isNull(_cursorIndexOfUsdSum)) {
            _tmp_3 = null;
          } else {
            _tmp_3 = cursor.getString(_cursorIndexOfUsdSum);
          }
          _tmpUsdSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_3);
          final LocalDateTime _tmpDate;
          final long _tmp_4;
          _tmp_4 = cursor.getLong(_cursorIndexOfDate);
          _tmpDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_4);
          final String _tmpDescription;
          if (cursor.isNull(_cursorIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = cursor.getString(_cursorIndexOfDescription);
          }
          final String _tmpQrCode;
          if (cursor.isNull(_cursorIndexOfQrCode)) {
            _tmpQrCode = null;
          } else {
            _tmpQrCode = cursor.getString(_cursorIndexOfQrCode);
          }
          final boolean _tmpIsRegular;
          final int _tmp_5;
          _tmp_5 = cursor.getInt(_cursorIndexOfIsRegular);
          _tmpIsRegular = _tmp_5 != 0;
          final boolean _tmpInStatistics;
          final int _tmp_6;
          _tmp_6 = cursor.getInt(_cursorIndexOfInStatistics);
          _tmpInStatistics = _tmp_6 != 0;
          final double _tmpLatitude;
          _tmpLatitude = cursor.getDouble(_cursorIndexOfLatitude);
          final double _tmpLongitude;
          _tmpLongitude = cursor.getDouble(_cursorIndexOfLongitude);
          final String _tmpPhotoUri;
          if (cursor.isNull(_cursorIndexOfPhotoUri)) {
            _tmpPhotoUri = null;
          } else {
            _tmpPhotoUri = cursor.getString(_cursorIndexOfPhotoUri);
          }
          _item = new TransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpCategoryId,_tmpCurrency,_tmpSum,_tmpUsdSum,_tmpDate,_tmpDescription,_tmpQrCode,_tmpIsRegular,_tmpInStatistics,_tmpLatitude,_tmpLongitude,_tmpPhotoUri);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Flow<List<TransactionDbModel>> getAllByTypeInPeriod(final String clientId,
      final LocalDateTime from, final LocalDateTime to, final int type) {
    final String _sql = "SELECT * FROM transactions WHERE clientId=? AND type = ? AND date >= ? AND date <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (clientId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, clientId);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, type);
    _argIndex = 3;
    final long _tmp = DateConverter.INSTANCE.fromOffsetDateTime(from);
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 4;
    final long _tmp_1 = DateConverter.INSTANCE.fromOffsetDateTime(to);
    _statement.bindLong(_argIndex, _tmp_1);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionDbModel>>() {
      @Override
      @NonNull
      public List<TransactionDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfUsdSum = CursorUtil.getColumnIndexOrThrow(_cursor, "usd_sum");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCode");
          final int _cursorIndexOfIsRegular = CursorUtil.getColumnIndexOrThrow(_cursor, "isRegular");
          final int _cursorIndexOfInStatistics = CursorUtil.getColumnIndexOrThrow(_cursor, "inStatistics");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
          final List<TransactionDbModel> _result = new ArrayList<TransactionDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionDbModel _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpCurrency;
            if (_cursor.isNull(_cursorIndexOfCurrency)) {
              _tmpCurrency = null;
            } else {
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            }
            final BigDecimal _tmpSum;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfSum)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfSum);
            }
            _tmpSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_2);
            final BigDecimal _tmpUsdSum;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfUsdSum)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfUsdSum);
            }
            _tmpUsdSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_3);
            final LocalDateTime _tmpDate;
            final long _tmp_4;
            _tmp_4 = _cursor.getLong(_cursorIndexOfDate);
            _tmpDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_4);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpQrCode;
            if (_cursor.isNull(_cursorIndexOfQrCode)) {
              _tmpQrCode = null;
            } else {
              _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
            }
            final boolean _tmpIsRegular;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsRegular);
            _tmpIsRegular = _tmp_5 != 0;
            final boolean _tmpInStatistics;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfInStatistics);
            _tmpInStatistics = _tmp_6 != 0;
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item = new TransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpCategoryId,_tmpCurrency,_tmpSum,_tmpUsdSum,_tmpDate,_tmpDescription,_tmpQrCode,_tmpIsRegular,_tmpInStatistics,_tmpLatitude,_tmpLongitude,_tmpPhotoUri);
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
  public Flow<TransactionDbModel> getById(final String uid, final long id) {
    final String _sql = "SELECT * FROM transactions WHERE clientId =? AND id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<TransactionDbModel>() {
      @Override
      @NonNull
      public TransactionDbModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfUsdSum = CursorUtil.getColumnIndexOrThrow(_cursor, "usd_sum");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCode");
          final int _cursorIndexOfIsRegular = CursorUtil.getColumnIndexOrThrow(_cursor, "isRegular");
          final int _cursorIndexOfInStatistics = CursorUtil.getColumnIndexOrThrow(_cursor, "inStatistics");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
          final TransactionDbModel _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
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
            final BigDecimal _tmpUsdSum;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfUsdSum)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfUsdSum);
            }
            _tmpUsdSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_1);
            final LocalDateTime _tmpDate;
            final long _tmp_2;
            _tmp_2 = _cursor.getLong(_cursorIndexOfDate);
            _tmpDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpQrCode;
            if (_cursor.isNull(_cursorIndexOfQrCode)) {
              _tmpQrCode = null;
            } else {
              _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
            }
            final boolean _tmpIsRegular;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsRegular);
            _tmpIsRegular = _tmp_3 != 0;
            final boolean _tmpInStatistics;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfInStatistics);
            _tmpInStatistics = _tmp_4 != 0;
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _result = new TransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpCategoryId,_tmpCurrency,_tmpSum,_tmpUsdSum,_tmpDate,_tmpDescription,_tmpQrCode,_tmpIsRegular,_tmpInStatistics,_tmpLatitude,_tmpLongitude,_tmpPhotoUri);
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

  @Override
  public Object getByCategoryId(final String uid, final long categoryId,
      final Continuation<? super List<TransactionDbModel>> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE clientId =? AND categoryId =?";
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
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TransactionDbModel>>() {
      @Override
      @NonNull
      public List<TransactionDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfUsdSum = CursorUtil.getColumnIndexOrThrow(_cursor, "usd_sum");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCode");
          final int _cursorIndexOfIsRegular = CursorUtil.getColumnIndexOrThrow(_cursor, "isRegular");
          final int _cursorIndexOfInStatistics = CursorUtil.getColumnIndexOrThrow(_cursor, "inStatistics");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
          final List<TransactionDbModel> _result = new ArrayList<TransactionDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionDbModel _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
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
            final BigDecimal _tmpUsdSum;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfUsdSum)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfUsdSum);
            }
            _tmpUsdSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_1);
            final LocalDateTime _tmpDate;
            final long _tmp_2;
            _tmp_2 = _cursor.getLong(_cursorIndexOfDate);
            _tmpDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpQrCode;
            if (_cursor.isNull(_cursorIndexOfQrCode)) {
              _tmpQrCode = null;
            } else {
              _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
            }
            final boolean _tmpIsRegular;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsRegular);
            _tmpIsRegular = _tmp_3 != 0;
            final boolean _tmpInStatistics;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfInStatistics);
            _tmpInStatistics = _tmp_4 != 0;
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item = new TransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpCategoryId,_tmpCurrency,_tmpSum,_tmpUsdSum,_tmpDate,_tmpDescription,_tmpQrCode,_tmpIsRegular,_tmpInStatistics,_tmpLatitude,_tmpLongitude,_tmpPhotoUri);
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

  @Override
  public Flow<List<TransactionDbModel>> getByCategoryIdInPeriod(final String uid,
      final long categoryId, final LocalDateTime from, final LocalDateTime to) {
    final String _sql = "SELECT * FROM transactions WHERE clientId =? AND categoryId =? AND date >= ? AND date <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, categoryId);
    _argIndex = 3;
    final long _tmp = DateConverter.INSTANCE.fromOffsetDateTime(from);
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 4;
    final long _tmp_1 = DateConverter.INSTANCE.fromOffsetDateTime(to);
    _statement.bindLong(_argIndex, _tmp_1);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionDbModel>>() {
      @Override
      @NonNull
      public List<TransactionDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfSum = CursorUtil.getColumnIndexOrThrow(_cursor, "sum");
          final int _cursorIndexOfUsdSum = CursorUtil.getColumnIndexOrThrow(_cursor, "usd_sum");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCode");
          final int _cursorIndexOfIsRegular = CursorUtil.getColumnIndexOrThrow(_cursor, "isRegular");
          final int _cursorIndexOfInStatistics = CursorUtil.getColumnIndexOrThrow(_cursor, "inStatistics");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photo");
          final List<TransactionDbModel> _result = new ArrayList<TransactionDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionDbModel _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final String _tmpCurrency;
            if (_cursor.isNull(_cursorIndexOfCurrency)) {
              _tmpCurrency = null;
            } else {
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            }
            final BigDecimal _tmpSum;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfSum)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfSum);
            }
            _tmpSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_2);
            final BigDecimal _tmpUsdSum;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfUsdSum)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfUsdSum);
            }
            _tmpUsdSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp_3);
            final LocalDateTime _tmpDate;
            final long _tmp_4;
            _tmp_4 = _cursor.getLong(_cursorIndexOfDate);
            _tmpDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_4);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpQrCode;
            if (_cursor.isNull(_cursorIndexOfQrCode)) {
              _tmpQrCode = null;
            } else {
              _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
            }
            final boolean _tmpIsRegular;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsRegular);
            _tmpIsRegular = _tmp_5 != 0;
            final boolean _tmpInStatistics;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfInStatistics);
            _tmpInStatistics = _tmp_6 != 0;
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item = new TransactionDbModel(_tmpId,_tmpClientId,_tmpType,_tmpCategoryId,_tmpCurrency,_tmpSum,_tmpUsdSum,_tmpDate,_tmpDescription,_tmpQrCode,_tmpIsRegular,_tmpInStatistics,_tmpLatitude,_tmpLongitude,_tmpPhotoUri);
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
  public Object getItemsCountInDay(final String uid, final int type, final LocalDateTime from,
      final LocalDateTime to, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT count(*) FROM transactions WHERE clientId=? AND type=? AND date >= ? AND date <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, type);
    _argIndex = 3;
    final long _tmp = DateConverter.INSTANCE.fromOffsetDateTime(from);
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 4;
    final long _tmp_1 = DateConverter.INSTANCE.fromOffsetDateTime(to);
    _statement.bindLong(_argIndex, _tmp_1);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp_2;
            if (_cursor.isNull(0)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getInt(0);
            }
            _result = _tmp_2;
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
  public Object getCountByCurrencyCode(final String uid, final String code,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT count(*) FROM transactions WHERE clientId=? AND currency=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    if (code == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, code);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

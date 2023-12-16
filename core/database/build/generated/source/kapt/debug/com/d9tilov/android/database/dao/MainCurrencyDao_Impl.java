package com.d9tilov.android.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.d9tilov.android.database.entity.MainCurrencyDbModel;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MainCurrencyDao_Impl implements MainCurrencyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MainCurrencyDbModel> __insertionAdapterOfMainCurrencyDbModel;

  public MainCurrencyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMainCurrencyDbModel = new EntityInsertionAdapter<MainCurrencyDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `main_currency` (`id`,`clientId`,`code`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MainCurrencyDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getClientId());
        }
        if (entity.getCode() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCode());
        }
      }
    };
  }

  @Override
  public Object insert(final MainCurrencyDbModel mainCurrencyDbModel,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMainCurrencyDbModel.insert(mainCurrencyDbModel);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<MainCurrencyDbModel> get(final String uid) {
    final String _sql = "SELECT * FROM main_currency WHERE clientId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"main_currency"}, new Callable<MainCurrencyDbModel>() {
      @Override
      @Nullable
      public MainCurrencyDbModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final MainCurrencyDbModel _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpClientId;
            if (_cursor.isNull(_cursorIndexOfClientId)) {
              _tmpClientId = null;
            } else {
              _tmpClientId = _cursor.getString(_cursorIndexOfClientId);
            }
            final String _tmpCode;
            if (_cursor.isNull(_cursorIndexOfCode)) {
              _tmpCode = null;
            } else {
              _tmpCode = _cursor.getString(_cursorIndexOfCode);
            }
            _result = new MainCurrencyDbModel(_tmpId,_tmpClientId,_tmpCode);
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

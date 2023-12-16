package com.d9tilov.android.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.d9tilov.android.database.entity.UserDbModel;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserDbModel> __insertionAdapterOfUserDbModel;

  private final EntityDeletionOrUpdateAdapter<UserDbModel> __updateAdapterOfUserDbModel;

  private final SharedSQLiteStatement __preparedStmtOfDeleteUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserDbModel = new EntityInsertionAdapter<UserDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`uid`,`firstName`,`lastName`,`currentCurrencyCode`,`showPrepopulate`,`fiscalDay`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserDbModel entity) {
        if (entity.getUid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUid());
        }
        if (entity.getFirstName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFirstName());
        }
        if (entity.getLastName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLastName());
        }
        if (entity.getCurrentCurrencyCode() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCurrentCurrencyCode());
        }
        final int _tmp = entity.getShowPrepopulate() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getFiscalDay());
      }
    };
    this.__updateAdapterOfUserDbModel = new EntityDeletionOrUpdateAdapter<UserDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `uid` = ?,`firstName` = ?,`lastName` = ?,`currentCurrencyCode` = ?,`showPrepopulate` = ?,`fiscalDay` = ? WHERE `uid` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserDbModel entity) {
        if (entity.getUid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUid());
        }
        if (entity.getFirstName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFirstName());
        }
        if (entity.getLastName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLastName());
        }
        if (entity.getCurrentCurrencyCode() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCurrentCurrencyCode());
        }
        final int _tmp = entity.getShowPrepopulate() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getFiscalDay());
        if (entity.getUid() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getUid());
        }
      }
    };
    this.__preparedStmtOfDeleteUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM users WHERE uid=?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final UserDbModel user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserDbModel.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final UserDbModel user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserDbModel.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteUser(final String uid, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteUser.acquire();
        int _argIndex = 1;
        if (uid == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, uid);
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
          __preparedStmtOfDeleteUser.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserDbModel> getById(final String id) {
    final String _sql = "SELECT * FROM Users WHERE uid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"Users"}, new Callable<UserDbModel>() {
      @Override
      @Nullable
      public UserDbModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
          final int _cursorIndexOfCurrentCurrencyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "currentCurrencyCode");
          final int _cursorIndexOfShowPrepopulate = CursorUtil.getColumnIndexOrThrow(_cursor, "showPrepopulate");
          final int _cursorIndexOfFiscalDay = CursorUtil.getColumnIndexOrThrow(_cursor, "fiscalDay");
          final UserDbModel _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUid;
            if (_cursor.isNull(_cursorIndexOfUid)) {
              _tmpUid = null;
            } else {
              _tmpUid = _cursor.getString(_cursorIndexOfUid);
            }
            final String _tmpFirstName;
            if (_cursor.isNull(_cursorIndexOfFirstName)) {
              _tmpFirstName = null;
            } else {
              _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
            }
            final String _tmpLastName;
            if (_cursor.isNull(_cursorIndexOfLastName)) {
              _tmpLastName = null;
            } else {
              _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            }
            final String _tmpCurrentCurrencyCode;
            if (_cursor.isNull(_cursorIndexOfCurrentCurrencyCode)) {
              _tmpCurrentCurrencyCode = null;
            } else {
              _tmpCurrentCurrencyCode = _cursor.getString(_cursorIndexOfCurrentCurrencyCode);
            }
            final boolean _tmpShowPrepopulate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfShowPrepopulate);
            _tmpShowPrepopulate = _tmp != 0;
            final int _tmpFiscalDay;
            _tmpFiscalDay = _cursor.getInt(_cursorIndexOfFiscalDay);
            _result = new UserDbModel(_tmpUid,_tmpFirstName,_tmpLastName,_tmpCurrentCurrencyCode,_tmpShowPrepopulate,_tmpFiscalDay);
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
  public int getFiscalDay(final String id) {
    final String _sql = "SELECT fiscalDay FROM Users WHERE uid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Object showPrepopulate(final String id, final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT showPrepopulate FROM Users WHERE uid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
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

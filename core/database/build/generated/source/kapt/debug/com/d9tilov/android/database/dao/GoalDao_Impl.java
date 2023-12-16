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
import com.d9tilov.android.database.converters.DateConverter;
import com.d9tilov.android.database.entity.GoalDbModel;
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
public final class GoalDao_Impl implements GoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GoalDbModel> __insertionAdapterOfGoalDbModel;

  private final EntityDeletionOrUpdateAdapter<GoalDbModel> __deletionAdapterOfGoalDbModel;

  private final EntityDeletionOrUpdateAdapter<GoalDbModel> __updateAdapterOfGoalDbModel;

  public GoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGoalDbModel = new EntityInsertionAdapter<GoalDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `goal` (`id`,`clientId`,`currency`,`name`,`targetSum`,`createdDate`,`description`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalDbModel entity) {
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
        if (entity.getName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getName());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getTargetSum());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        final long _tmp_1 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getCreatedDate());
        statement.bindLong(6, _tmp_1);
        if (entity.getDescription() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDescription());
        }
      }
    };
    this.__deletionAdapterOfGoalDbModel = new EntityDeletionOrUpdateAdapter<GoalDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `goal` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalDbModel entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGoalDbModel = new EntityDeletionOrUpdateAdapter<GoalDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `goal` SET `id` = ?,`clientId` = ?,`currency` = ?,`name` = ?,`targetSum` = ?,`createdDate` = ?,`description` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalDbModel entity) {
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
        if (entity.getName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getName());
        }
        final String _tmp = CurrencyConverter.INSTANCE.fromBigDecimalToString(entity.getTargetSum());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        final long _tmp_1 = DateConverter.INSTANCE.fromOffsetDateTime(entity.getCreatedDate());
        statement.bindLong(6, _tmp_1);
        if (entity.getDescription() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDescription());
        }
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final GoalDbModel goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGoalDbModel.insert(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final GoalDbModel goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGoalDbModel.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final GoalDbModel goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGoalDbModel.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GoalDbModel>> getAll(final String uid) {
    final String _sql = "SELECT * FROM goal WHERE clientId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"goal"}, new Callable<List<GoalDbModel>>() {
      @Override
      @NonNull
      public List<GoalDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTargetSum = CursorUtil.getColumnIndexOrThrow(_cursor, "targetSum");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<GoalDbModel> _result = new ArrayList<GoalDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GoalDbModel _item;
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
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final BigDecimal _tmpTargetSum;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTargetSum)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTargetSum);
            }
            _tmpTargetSum = CurrencyConverter.INSTANCE.fromLongToBigDecimal(_tmp);
            final LocalDateTime _tmpCreatedDate;
            final long _tmp_1;
            _tmp_1 = _cursor.getLong(_cursorIndexOfCreatedDate);
            _tmpCreatedDate = DateConverter.INSTANCE.toOffsetDateTime(_tmp_1);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            _item = new GoalDbModel(_tmpId,_tmpClientId,_tmpCurrency,_tmpName,_tmpTargetSum,_tmpCreatedDate,_tmpDescription);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

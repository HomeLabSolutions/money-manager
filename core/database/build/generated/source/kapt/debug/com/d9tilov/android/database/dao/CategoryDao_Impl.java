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
import com.d9tilov.android.database.entity.CategoryDbModel;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
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
public final class CategoryDao_Impl implements CategoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CategoryDbModel> __insertionAdapterOfCategoryDbModel;

  private final EntityDeletionOrUpdateAdapter<CategoryDbModel> __updateAdapterOfCategoryDbModel;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public CategoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategoryDbModel = new EntityInsertionAdapter<CategoryDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `categories` (`id`,`clientId`,`parentId`,`type`,`name`,`icon`,`color`,`usageCount`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoryDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUid() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUid());
        }
        statement.bindLong(3, entity.getParentId());
        statement.bindLong(4, entity.getType());
        if (entity.getName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getName());
        }
        statement.bindLong(6, entity.getIconNameOrdinal());
        statement.bindLong(7, entity.getColorNameOrdinal());
        statement.bindLong(8, entity.getUsageCount());
      }
    };
    this.__updateAdapterOfCategoryDbModel = new EntityDeletionOrUpdateAdapter<CategoryDbModel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `categories` SET `id` = ?,`clientId` = ?,`parentId` = ?,`type` = ?,`name` = ?,`icon` = ?,`color` = ?,`usageCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoryDbModel entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUid() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUid());
        }
        statement.bindLong(3, entity.getParentId());
        statement.bindLong(4, entity.getType());
        if (entity.getName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getName());
        }
        statement.bindLong(6, entity.getIconNameOrdinal());
        statement.bindLong(7, entity.getColorNameOrdinal());
        statement.bindLong(8, entity.getUsageCount());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM categories WHERE clientId=? AND id=?";
        return _query;
      }
    };
  }

  @Override
  public Object create(final CategoryDbModel category,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCategoryDbModel.insertAndReturnId(category);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CategoryDbModel category,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCategoryDbModel.handle(category);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final String uid, final long id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        if (uid == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, uid);
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
  public Flow<List<CategoryDbModel>> getAllByType(final String uid, final int type) {
    final String _sql = "SELECT * FROM categories WHERE clientId=? AND type=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"categories"}, new Callable<List<CategoryDbModel>>() {
      @Override
      @NonNull
      public List<CategoryDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIconNameOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfColorNameOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
          final List<CategoryDbModel> _result = new ArrayList<CategoryDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryDbModel _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUid;
            if (_cursor.isNull(_cursorIndexOfUid)) {
              _tmpUid = null;
            } else {
              _tmpUid = _cursor.getString(_cursorIndexOfUid);
            }
            final long _tmpParentId;
            _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpIconNameOrdinal;
            _tmpIconNameOrdinal = _cursor.getInt(_cursorIndexOfIconNameOrdinal);
            final int _tmpColorNameOrdinal;
            _tmpColorNameOrdinal = _cursor.getInt(_cursorIndexOfColorNameOrdinal);
            final int _tmpUsageCount;
            _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
            _item = new CategoryDbModel(_tmpId,_tmpUid,_tmpParentId,_tmpType,_tmpName,_tmpIconNameOrdinal,_tmpColorNameOrdinal,_tmpUsageCount);
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
      final Continuation<? super CategoryDbModel> $completion) {
    final String _sql = "SELECT * FROM categories WHERE clientId=? AND id=?";
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
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CategoryDbModel>() {
      @Override
      @Nullable
      public CategoryDbModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIconNameOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfColorNameOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
          final CategoryDbModel _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUid;
            if (_cursor.isNull(_cursorIndexOfUid)) {
              _tmpUid = null;
            } else {
              _tmpUid = _cursor.getString(_cursorIndexOfUid);
            }
            final long _tmpParentId;
            _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpIconNameOrdinal;
            _tmpIconNameOrdinal = _cursor.getInt(_cursorIndexOfIconNameOrdinal);
            final int _tmpColorNameOrdinal;
            _tmpColorNameOrdinal = _cursor.getInt(_cursorIndexOfColorNameOrdinal);
            final int _tmpUsageCount;
            _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
            _result = new CategoryDbModel(_tmpId,_tmpUid,_tmpParentId,_tmpType,_tmpName,_tmpIconNameOrdinal,_tmpColorNameOrdinal,_tmpUsageCount);
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
  public Object getCategoriesCountByName(final String uid, final String name,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM categories WHERE clientId=? AND name=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
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

  @Override
  public Flow<List<CategoryDbModel>> getByParentId(final String uid, final long id) {
    final String _sql = "SELECT * FROM categories WHERE clientId=? AND parentId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (uid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uid);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"categories"}, new Callable<List<CategoryDbModel>>() {
      @Override
      @NonNull
      public List<CategoryDbModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "clientId");
          final int _cursorIndexOfParentId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIconNameOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfColorNameOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
          final List<CategoryDbModel> _result = new ArrayList<CategoryDbModel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryDbModel _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUid;
            if (_cursor.isNull(_cursorIndexOfUid)) {
              _tmpUid = null;
            } else {
              _tmpUid = _cursor.getString(_cursorIndexOfUid);
            }
            final long _tmpParentId;
            _tmpParentId = _cursor.getLong(_cursorIndexOfParentId);
            final int _tmpType;
            _tmpType = _cursor.getInt(_cursorIndexOfType);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpIconNameOrdinal;
            _tmpIconNameOrdinal = _cursor.getInt(_cursorIndexOfIconNameOrdinal);
            final int _tmpColorNameOrdinal;
            _tmpColorNameOrdinal = _cursor.getInt(_cursorIndexOfColorNameOrdinal);
            final int _tmpUsageCount;
            _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
            _item = new CategoryDbModel(_tmpId,_tmpUid,_tmpParentId,_tmpType,_tmpName,_tmpIconNameOrdinal,_tmpColorNameOrdinal,_tmpUsageCount);
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

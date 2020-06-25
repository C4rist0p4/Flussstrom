package com.example.myapplication.database.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.myapplication.database.entiy.Meldungen;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MeldungenDao_Impl implements MeldungenDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Meldungen> __insertionAdapterOfMeldungen;

  private final EntityDeletionOrUpdateAdapter<Meldungen> __deletionAdapterOfMeldungen;

  private final EntityDeletionOrUpdateAdapter<Meldungen> __updateAdapterOfMeldungen;

  public MeldungenDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMeldungen = new EntityInsertionAdapter<Meldungen>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Meldungen` (`uid`,`SystemName`,`datum`,`meldungstyp`,`bemerkungMel`,`timestamp_device`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Meldungen value) {
        stmt.bindLong(1, value.getUid());
        if (value.getSystemName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSystemName());
        }
        if (value.getDatum() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDatum());
        }
        if (value.getMeldungstyp() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMeldungstyp());
        }
        if (value.getBemerkungMel() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getBemerkungMel());
        }
        if (value.getTimestamp_device() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getTimestamp_device());
        }
      }
    };
    this.__deletionAdapterOfMeldungen = new EntityDeletionOrUpdateAdapter<Meldungen>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Meldungen` WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Meldungen value) {
        stmt.bindLong(1, value.getUid());
      }
    };
    this.__updateAdapterOfMeldungen = new EntityDeletionOrUpdateAdapter<Meldungen>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Meldungen` SET `uid` = ?,`SystemName` = ?,`datum` = ?,`meldungstyp` = ?,`bemerkungMel` = ?,`timestamp_device` = ? WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Meldungen value) {
        stmt.bindLong(1, value.getUid());
        if (value.getSystemName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSystemName());
        }
        if (value.getDatum() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDatum());
        }
        if (value.getMeldungstyp() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMeldungstyp());
        }
        if (value.getBemerkungMel() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getBemerkungMel());
        }
        if (value.getTimestamp_device() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getTimestamp_device());
        }
        stmt.bindLong(7, value.getUid());
      }
    };
  }

  @Override
  public void insert(final Meldungen meldungen) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMeldungen.insert(meldungen);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Meldungen meldungen) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMeldungen.handle(meldungen);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Meldungen meldungen) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfMeldungen.handle(meldungen);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Meldungen>> getAll() {
    final String _sql = "SELECT * FROM meldungen";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"meldungen"}, false, new Callable<List<Meldungen>>() {
      @Override
      public List<Meldungen> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfSystemName = CursorUtil.getColumnIndexOrThrow(_cursor, "SystemName");
          final int _cursorIndexOfDatum = CursorUtil.getColumnIndexOrThrow(_cursor, "datum");
          final int _cursorIndexOfMeldungstyp = CursorUtil.getColumnIndexOrThrow(_cursor, "meldungstyp");
          final int _cursorIndexOfBemerkungMel = CursorUtil.getColumnIndexOrThrow(_cursor, "bemerkungMel");
          final int _cursorIndexOfTimestampDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp_device");
          final List<Meldungen> _result = new ArrayList<Meldungen>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Meldungen _item;
            final String _tmpSystemName;
            _tmpSystemName = _cursor.getString(_cursorIndexOfSystemName);
            final String _tmpDatum;
            _tmpDatum = _cursor.getString(_cursorIndexOfDatum);
            final String _tmpMeldungstyp;
            _tmpMeldungstyp = _cursor.getString(_cursorIndexOfMeldungstyp);
            final String _tmpBemerkungMel;
            _tmpBemerkungMel = _cursor.getString(_cursorIndexOfBemerkungMel);
            final String _tmpTimestamp_device;
            _tmpTimestamp_device = _cursor.getString(_cursorIndexOfTimestampDevice);
            _item = new Meldungen(_tmpSystemName,_tmpDatum,_tmpMeldungstyp,_tmpBemerkungMel,_tmpTimestamp_device);
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            _item.setUid(_tmpUid);
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
  public LiveData<List<Meldungen>> getAllbySystemName(final String systemName) {
    final String _sql = "SELECT * FROM meldungen WHERE SystemName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (systemName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, systemName);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"meldungen"}, false, new Callable<List<Meldungen>>() {
      @Override
      public List<Meldungen> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfSystemName = CursorUtil.getColumnIndexOrThrow(_cursor, "SystemName");
          final int _cursorIndexOfDatum = CursorUtil.getColumnIndexOrThrow(_cursor, "datum");
          final int _cursorIndexOfMeldungstyp = CursorUtil.getColumnIndexOrThrow(_cursor, "meldungstyp");
          final int _cursorIndexOfBemerkungMel = CursorUtil.getColumnIndexOrThrow(_cursor, "bemerkungMel");
          final int _cursorIndexOfTimestampDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp_device");
          final List<Meldungen> _result = new ArrayList<Meldungen>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Meldungen _item;
            final String _tmpSystemName;
            _tmpSystemName = _cursor.getString(_cursorIndexOfSystemName);
            final String _tmpDatum;
            _tmpDatum = _cursor.getString(_cursorIndexOfDatum);
            final String _tmpMeldungstyp;
            _tmpMeldungstyp = _cursor.getString(_cursorIndexOfMeldungstyp);
            final String _tmpBemerkungMel;
            _tmpBemerkungMel = _cursor.getString(_cursorIndexOfBemerkungMel);
            final String _tmpTimestamp_device;
            _tmpTimestamp_device = _cursor.getString(_cursorIndexOfTimestampDevice);
            _item = new Meldungen(_tmpSystemName,_tmpDatum,_tmpMeldungstyp,_tmpBemerkungMel,_tmpTimestamp_device);
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            _item.setUid(_tmpUid);
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
}

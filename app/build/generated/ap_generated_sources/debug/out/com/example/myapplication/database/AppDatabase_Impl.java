package com.example.myapplication.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.example.myapplication.database.dao.MeldungenDao;
import com.example.myapplication.database.dao.MeldungenDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MeldungenDao _meldungenDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Meldungen` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `SystemName` TEXT, `datum` TEXT, `meldungstyp` TEXT, `bemerkungMel` TEXT, `timestamp_device` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Meldungstyp` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `idMeldungstyp` INTEGER NOT NULL, `meldungsart` TEXT, `bemerkungMT` TEXT, `versandart` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7a4c7beb96c1faff584f9f8afbbf3d70')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Meldungen`");
        _db.execSQL("DROP TABLE IF EXISTS `Meldungstyp`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsMeldungen = new HashMap<String, TableInfo.Column>(6);
        _columnsMeldungen.put("uid", new TableInfo.Column("uid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungen.put("SystemName", new TableInfo.Column("SystemName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungen.put("datum", new TableInfo.Column("datum", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungen.put("meldungstyp", new TableInfo.Column("meldungstyp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungen.put("bemerkungMel", new TableInfo.Column("bemerkungMel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungen.put("timestamp_device", new TableInfo.Column("timestamp_device", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeldungen = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeldungen = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeldungen = new TableInfo("Meldungen", _columnsMeldungen, _foreignKeysMeldungen, _indicesMeldungen);
        final TableInfo _existingMeldungen = TableInfo.read(_db, "Meldungen");
        if (! _infoMeldungen.equals(_existingMeldungen)) {
          return new RoomOpenHelper.ValidationResult(false, "Meldungen(com.example.myapplication.database.entiy.Meldungen).\n"
                  + " Expected:\n" + _infoMeldungen + "\n"
                  + " Found:\n" + _existingMeldungen);
        }
        final HashMap<String, TableInfo.Column> _columnsMeldungstyp = new HashMap<String, TableInfo.Column>(5);
        _columnsMeldungstyp.put("uid", new TableInfo.Column("uid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungstyp.put("idMeldungstyp", new TableInfo.Column("idMeldungstyp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungstyp.put("meldungsart", new TableInfo.Column("meldungsart", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungstyp.put("bemerkungMT", new TableInfo.Column("bemerkungMT", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMeldungstyp.put("versandart", new TableInfo.Column("versandart", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeldungstyp = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeldungstyp = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeldungstyp = new TableInfo("Meldungstyp", _columnsMeldungstyp, _foreignKeysMeldungstyp, _indicesMeldungstyp);
        final TableInfo _existingMeldungstyp = TableInfo.read(_db, "Meldungstyp");
        if (! _infoMeldungstyp.equals(_existingMeldungstyp)) {
          return new RoomOpenHelper.ValidationResult(false, "Meldungstyp(com.example.myapplication.database.entiy.Meldungstyp).\n"
                  + " Expected:\n" + _infoMeldungstyp + "\n"
                  + " Found:\n" + _existingMeldungstyp);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "7a4c7beb96c1faff584f9f8afbbf3d70", "153889c5d72daf720a432bde4cacae46");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Meldungen","Meldungstyp");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Meldungen`");
      _db.execSQL("DELETE FROM `Meldungstyp`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public MeldungenDao meldDao() {
    if (_meldungenDao != null) {
      return _meldungenDao;
    } else {
      synchronized(this) {
        if(_meldungenDao == null) {
          _meldungenDao = new MeldungenDao_Impl(this);
        }
        return _meldungenDao;
      }
    }
  }
}

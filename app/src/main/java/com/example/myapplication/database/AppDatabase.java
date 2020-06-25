package com.example.myapplication.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.myapplication.database.converter.MeldungstypeConverter;
import com.example.myapplication.database.dao.MeldungenDao;
import com.example.myapplication.database.entiy.Meldungen;
import com.example.myapplication.database.entiy.Meldungstyp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Meldungen.class, Meldungstyp.class}, version = 1, exportSchema = false)
@TypeConverters({MeldungstypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MeldungenDao meldDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "flussstrom_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}

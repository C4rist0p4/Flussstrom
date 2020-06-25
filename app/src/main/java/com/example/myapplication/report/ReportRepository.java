package com.example.myapplication.report;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.dao.MeldungenDao;
import com.example.myapplication.database.entiy.Meldungen;

import java.util.List;

public class ReportRepository {
    private MeldungenDao meldungenDao;
    private LiveData<List<Meldungen>> allMeldungen;
    private LiveData<List<Meldungen>> allbySystemName;

    public ReportRepository(Application application, String systemName) {
        AppDatabase db = AppDatabase.getDatabase(application);
        meldungenDao = db.meldDao();
        allMeldungen = meldungenDao.getAll();
        allbySystemName = meldungenDao.getAllbySystemName(systemName);

    }

    public void insert(Meldungen meldungen) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            meldungenDao.insert(meldungen);
        });
    }

    public void update(Meldungen meldungen) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            meldungenDao.update(meldungen);
        });

    }

    public void deleteAllMeldungen(Meldungen meldungen) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            meldungenDao.delete(meldungen);
        });
    }

    public LiveData<List<Meldungen>> getAllMeldungen() {
        return allMeldungen;
    }

    public LiveData<List<Meldungen>> getAllbySystemName(String systemName) { return allbySystemName; }

}

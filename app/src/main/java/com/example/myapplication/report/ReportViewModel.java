package com.example.myapplication.report;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.entiy.Meldungen;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    ReportRepository reportRepository;
    private LiveData<List<Meldungen>> allMeldungne;
    private LiveData<List<Meldungen>> allbySystemName;
    private Meldungen[] countValues;

    public ReportViewModel(@NonNull Application application, String systemName) {
        super(application);
        reportRepository = new ReportRepository(application, systemName);
        allMeldungne = reportRepository.getAllMeldungen();
        allbySystemName = reportRepository.getAllbySystemName(systemName);
    }

    public void insert(Meldungen meldungen) {
        reportRepository.insert(meldungen);
    }

    public void update(Meldungen meldungen) {
        reportRepository.update(meldungen);
    }

    public void delete(Meldungen meldungen) {
        reportRepository.deleteAllMeldungen(meldungen);
    }

    public LiveData<List<Meldungen>> getAllMeldungne() {
        return allMeldungne;
    }

    public LiveData<List<Meldungen>> getAllbySystemName(String systemName) {
        return allbySystemName;
    }

}

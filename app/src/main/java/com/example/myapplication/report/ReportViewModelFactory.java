package com.example.myapplication.report;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ReportViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;

    public  ReportViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ReportViewModel(mApplication, mParam);
    }
}

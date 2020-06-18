package com.example.myapplication.pictureDownload;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.myapplication.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_circle, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(null);
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}

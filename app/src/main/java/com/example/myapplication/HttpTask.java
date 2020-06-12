package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpTask extends AsyncTask<String, Void, String[]> {
    private OnEventListener<String> mCallBack;
    private Exception mException;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;

    public HttpTask(Context context, ProgressBar pBar, OnEventListener<String> callback) {
        mCallBack = callback;
        WeakReference<Context> contextRef = new WeakReference<>(context);
        progressBar = pBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String[] doInBackground(String... strURLs) {
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(strURLs[0]);
            conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return new String[] {result.toString()};
            } else {
                return new String[] {"Fail (" + responseCode + ")"};
            }
        } catch (IOException e) {
            mException = e;
        }
        return  null;
    }

    @Override
    protected void onPostExecute(String[] result) {
       progressBar.setVisibility(View.GONE);
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(result);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}
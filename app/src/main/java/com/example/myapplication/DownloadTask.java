package com.example.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class DownloadTask extends AsyncTask<String, Void, String[]> {
    private String username = BuildConfig.username;
    private String password = BuildConfig.password;
    private String address = BuildConfig.address;

    private OnEventListener<String> mCallBack;
    public Exception mException;
    private WeakReference<Context> contextRef;

    public DownloadTask(Context context, OnEventListener<String> callback) {
        mCallBack = callback;
        contextRef = new WeakReference<>(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String[] doInBackground(String... params) {

        FTPClient client = new FTPClient();
        try {
            client.connect(address);
            client.enterLocalPassiveMode();
            client.login(username, password);

            String[] splitPath = params[0].split("/");

            client.changeWorkingDirectory(splitPath[3]);

            FTPFile file = client.mdtmFile(splitPath[4]);
            String fliename = splitPath[4];
            Calendar calendar = file.getTimestamp();
            client.setFileType(FTP.BINARY_FILE_TYPE);

            ContextWrapper cw = new ContextWrapper(contextRef.get());

            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File mypath = new File(directory, fliename);

            FileOutputStream  fis = new FileOutputStream(mypath);
            client.retrieveFile(fliename, fis);

            client.logout();

            Date d = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


            return new String[] {mypath.toString(), dateFormat.format(d), timeFormat.format(d)};
        }
        catch (Exception e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(result);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}
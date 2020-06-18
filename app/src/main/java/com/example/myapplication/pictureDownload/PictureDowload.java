package com.example.myapplication.pictureDownload;

import android.content.Context;
import android.content.ContextWrapper;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.OnEventListener;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PictureDowload extends BaseTask {
    private String username = BuildConfig.username;
    private String password = BuildConfig.password;
    private String address = BuildConfig.address;
    private String path;
    private Context context;
    public Exception mException;
    private OnEventListener<String> mCallBack;
    private ProgressBar progressBar;

    public PictureDowload(Context context, String path, ProgressBar progressBar ,OnEventListener<String> callback) {
        this.path = path;
        this.context = context;
        this.mCallBack = callback;
        this.progressBar = progressBar;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Object call() {
        FTPClient client = new FTPClient();
        try {
            client.connect(address);
            client.enterLocalPassiveMode();
            client.login(username, password);

            String[] splitPath = path.split("/");

            client.changeWorkingDirectory(splitPath[3]);

            FTPFile file = client.mdtmFile(splitPath[4]);
            String fliename = splitPath[4];
            Calendar calendar = file.getTimestamp();
            client.setFileType(FTP.BINARY_FILE_TYPE);

            ContextWrapper cw = new ContextWrapper(context);

            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File mypath = new File(directory, fliename);

            FileOutputStream fis = new FileOutputStream(mypath);
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
    public void setDataAfterLoading(Object result) {
        progressBar.setVisibility(View.GONE);
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess((String[]) result);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}

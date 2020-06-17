package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;


public class Pictures extends Fragment {
    ImageView image;
    TextView date;
    TextView time;
    String systemName = null;
    FirebaseFunctions mFunctions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pictures, container, false);

        image = (ImageView) view.findViewById(R.id.imageView);
        date = (TextView) view.findViewById(R.id.dateTV);
        time = (TextView) view.findViewById(R.id.timeTV);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            systemName = bundle.getString("SystemName");
            getPicture(systemName);
        }
    }

    private void getPicture(String sysname) {
        addMessage(sysname)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("TAG", "addMessage:onFailure", e);
                        Toast.makeText(requireActivity().getApplicationContext(), "An error occurred."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HashMap result = task.getResult();
                    assert result != null;
                    DownloadPicture(result.get("pictureURL").toString());
                });
    }

    private Task<HashMap> addMessage(String sysname) {
        DatabaseHelper db = new DatabaseHelper(getActivity());

        HashMap systemDetails = db.getSystemDetails(sysname);
        String idAnlagen = systemDetails.get("idAnlagen").toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("fk_anlagen", idAnlagen);

        return mFunctions
                .getHttpsCallable("getPictures")
                .call(data)
                .continueWith(task -> (HashMap) Objects.requireNonNull(task.getResult()).getData());
    }


    private void DownloadPicture(String path) {
        DownloadTask downloadTask = new DownloadTask(requireActivity().getApplicationContext(), new OnEventListener<String>() {

            @Override
            public void onSuccess(String[] object) {

                File imgFile = new File(object[0]);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                date.setText(object[1]);
                time.setText(object[2]);
                image.setImageBitmap(myBitmap);

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(requireActivity().getApplicationContext(), "Download Fail", Toast.LENGTH_LONG).show();
            }
        });

       downloadTask.execute(path);
    }
}

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.pictureDownload.PictureDowload;
import com.example.myapplication.pictureDownload.TaskRunner;
import com.example.myapplication.swipeGesture.SwipeGestureDetector;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Pictures extends Fragment {
    ImageView image;
    TextView date;
    TextView time;
    String systemName = null;
    FirebaseFunctions mFunctions;
    ProgressBar progressBar;
    Spinner spinner;
    private SwipeGestureDetector swipeGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();

        swipeGestureDetector=new SwipeGestureDetector(() -> {
            progressBar.setVisibility(View.VISIBLE);
            getPicture(systemName);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pictures, container, false);

        image = (ImageView) view.findViewById(R.id.imageView);
        date = (TextView) view.findViewById(R.id.dateTV);
        time = (TextView) view.findViewById(R.id.timeTV);
        progressBar = view.findViewById(R.id.progressBar);
        spinner = view.findViewById(R.id.spinner);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            systemName = bundle.getString("SystemName");
            progressBar.setVisibility(View.VISIBLE);
            getPicture(systemName);
        }
    }

    private void getPicture(String sysname) {
        getPictue(sysname)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("TAG", "addMessage:onFailure", e);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireActivity().getApplicationContext(), "An error occurred."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<HashMap> result = (ArrayList<HashMap>) task.getResult();
                    assert result != null;
                    PictureToSpinner(result);
                });
    }

    private Task<ArrayList<HashMap>> getPictue(String sysname) {
        DatabaseHelper db = new DatabaseHelper(getActivity());

        HashMap systemDetails = db.getSystemDetails(sysname);
        String idAnlagen = systemDetails.get("idAnlagen").toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("fk_anlagen", idAnlagen);

        return mFunctions
                .getHttpsCallable("getPictures")
                .call(data)
                .continueWith(task -> (ArrayList<HashMap>) Objects.requireNonNull(task.getResult()).getData());
    }

    private void PictureToSpinner(ArrayList<HashMap> pictures) {
        List<String> picturesURL = new ArrayList<>();

        for (HashMap picture : pictures){

            String url = Objects.requireNonNull(picture.get("datum")).toString();
            picturesURL.add(url);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, picturesURL);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap picture = pictures.get(position);
                String url = (String) picture.get("url");


                DownloadPicture(url);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void DownloadPicture(String path) {

        PictureDowload pictureDowload = new PictureDowload(requireActivity().getApplicationContext(), path, progressBar, new OnEventListener<String>() {
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

        TaskRunner runner = new TaskRunner();
        runner.executeAsync(pictureDowload);
    }
}

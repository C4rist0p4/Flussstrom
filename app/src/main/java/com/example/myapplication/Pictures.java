package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class Pictures extends Fragment {
    ImageView image;
    TextView date;
    TextView time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pictures, container, false);

        image = (ImageView) view.findViewById(R.id.imageView);
        date = (TextView) view.findViewById(R.id.dateTV);
        time = (TextView) view.findViewById(R.id.timeTV);

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

        downloadTask.execute();

        return view;
    }
}

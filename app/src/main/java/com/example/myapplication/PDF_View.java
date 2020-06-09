package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDF_View extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f__view);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        pdfView = findViewById(R.id.pdfView);

        File pdf = new File(path);
        pdfView.fromFile(pdf).load();
    }
}
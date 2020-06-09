package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class AssetMasterData extends Fragment {
    private FirebaseFunctions mFunctions;
    private TextView systemname;
    private TextView installationLocation;
    private TextView installation;
    private TextView serialNumber;
    private TextView power;
    private TextView connectedValues;
    private TextView dataConnection;
    private TextView draft;
    private TextView dataSheet;
    private TextView comment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_master_data, container, false);
        systemname = view.findViewById(R.id.systemname);
        installationLocation = view.findViewById(R.id.installationLocation);
        installation = view.findViewById(R.id.installation);
        serialNumber = view.findViewById(R.id.serialNumber);
        power = view.findViewById(R.id.power);
        connectedValues = view.findViewById(R.id.connectedValues);
        dataConnection = view.findViewById(R.id.dataConnection);
        draft = view.findViewById(R.id.draft);
        dataSheet = view.findViewById(R.id.dataSheet);
        comment = view.findViewById(R.id.comment);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String name = bundle.getString("SystemName");
            getMachineryData(name);
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void getMachineryData(String name) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        HashMap SystemId = db.getSystemDetails(name);

        showMachineryData(SystemId);
    }


    private void showMachineryData(HashMap data) {
        systemname.append(Objects.requireNonNull(data.get("anlagenname")).toString());
        installationLocation.append(Objects.requireNonNull(data.get("installationsort")).toString());
        installation.append(Objects.requireNonNull(data.get("inbetriebnahme")).toString());
        serialNumber.append(Objects.requireNonNull(data.get("seriennummer")).toString());
        comment.append(Objects.requireNonNull(data.get("bemerkung")).toString());
        power.append(Objects.requireNonNull(data.get("leistung")).toString());
        connectedValues.append(Objects.requireNonNull(data.get("anschlusswerte")).toString());
        dataConnection.append(Objects.requireNonNull(data.get("datenanschluss")).toString());
        draft.append(Objects.requireNonNull(data.get("tiefgang")).toString());

        dataSheet.append(Objects.requireNonNull(data.get("datenblatt")).toString());

        String text = data.get("datenblatt").toString();

        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(@NonNull View widget) {

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference gsReference = storage.getReferenceFromUrl("gs://projekt-f-545a1.appspot.com/PDF/Lorem ipsum.pdf");

                try {
                    File localFile = File.createTempFile("datenblatt", ".pdf");

                    File finalLocalFile = localFile;
                    gsReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {

                        Intent intent = new Intent(requireActivity().getApplicationContext(), PDF_View.class);

                        String path = finalLocalFile.getPath();
                        intent.putExtra("path", path);
                        startActivity(intent);

                }).addOnFailureListener(exception -> {
                    Toast.makeText(requireActivity().getApplicationContext(), "Download Fail", Toast.LENGTH_LONG).show();
                });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        spannableString.setSpan(clickableSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        dataSheet.setText(spannableString);
        dataSheet.setMovementMethod(LinkMovementMethod.getInstance());
    }

}

package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Report extends Fragment {
    private FirebaseFunctions mFunctions;
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private ArrayList<ReportItme> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
        arrayList =  new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        SQLiteDatabase sqLiteDatabase = getDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Meldungen", null);

        if(cursor != null && cursor.getCount()>0){
            databaseToUI(cursor);
        }
        else {
            getData();
        }
        return view;
    }

    @SuppressLint("SQLiteString")
    private SQLiteDatabase getDatabase() {
        SQLiteDatabase sqLiteDatabase = Objects.requireNonNull(getActivity()).openOrCreateDatabase("Flussstrom", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Meldungen (datum TEXT, fk_meldungstyp TEXT, bemerkungMel TEXT)");

        return sqLiteDatabase;
    }

    private void databaseToUI(Cursor cursor) {
        try {
            int datumIndex = cursor.getColumnIndex("datum");
            int fk_meldungstypIndex = cursor.getColumnIndex("fk_meldungstyp");
            int bemerkungMelIndex = cursor.getColumnIndex("bemerkungMel");

            if (cursor.moveToFirst()) {
                do {
                    String datum = cursor.getString(datumIndex);
                    String bemerkungMel = cursor.getString(fk_meldungstypIndex);
                    String fk_meldungstyp = cursor.getString(bemerkungMelIndex);

                    arrayList.add(new ReportItme(datum,  fk_meldungstyp, bemerkungMel));
                }
                while ( cursor.moveToNext());
            }
            reportAdapter = new ReportAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(reportAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void safeData(ArrayList data) {
        SQLiteDatabase sqLiteDatabase = getDatabase();
        ContentValues cv = new  ContentValues();

        for(int i=0; i < data.size(); i++){
            HashMap<String, String> meldungen = (HashMap<String, String>) data.get(i);

            String datum_ = meldungen.get("datum");
            String bemerkungMel_ = meldungen.get("bemerkungMel");
            String fk_meldungstyp_ = String.valueOf(meldungen.get("fk_meldungstyp"));

            cv.put("datum",    datum_);
            cv.put("bemerkungMel",   bemerkungMel_);
            cv.put("fk_meldungstyp",   fk_meldungstyp_);

            sqLiteDatabase.insert( "Meldungen", null, cv );
        }
    }

    // TODO Datum Abfrage hinzufügen
    private void getData() {
        addMessage()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("TAG", "addMessage:onFailure", e);
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "An error occurred."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList result = task.getResult();
                    assert result != null;
                    showData(result);
                    safeData(result);
                });
    }

    private Task<ArrayList> addMessage() {
        return mFunctions
                .getHttpsCallable("getMeldung")
                .call()
                .continueWith(task -> {
                    ArrayList<String> result;
                    try {
                        result = (ArrayList<String>) Objects.requireNonNull(task.getResult()).getData();
                        assert result != null;
                        return result;
                    } catch (Exception e) {
                        throw new Exception(e);
                    }
                });
    }

    private void showData(ArrayList data){
        try {
            for (int i = 0; i < data.size(); i++){
                    HashMap<String, String> meldungen = (HashMap<String, String>) data.get(i);

                    String datum = meldungen.get("datum");
                    String bemerkungMel = meldungen.get("bemerkungMel");
                    String fk_meldungstyp = String.valueOf(meldungen.get("fk_meldungstyp"));

                    arrayList.add(new ReportItme(datum,  fk_meldungstyp, bemerkungMel));
            }
            reportAdapter = new ReportAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(reportAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

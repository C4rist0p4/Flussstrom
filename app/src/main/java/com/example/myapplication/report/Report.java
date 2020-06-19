package com.example.myapplication.report;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.swipeGesture.SwipeActions;
import com.example.myapplication.swipeGesture.SwipeGestureDetector;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Report extends Fragment {
    private FirebaseFunctions mFunctions;
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private ArrayList<ReportItem> arrayList;
    private String systemName = null;
    ProgressBar progressBar;
    private SwipeGestureDetector swipeGestureDetector;
    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
        arrayList =  new ArrayList<>();

        swipeGestureDetector=new SwipeGestureDetector(() -> {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.clearAnimation();
            getData(systemName);
        });

        gestureDetectorCompat = new GestureDetectorCompat(requireActivity().getApplicationContext(), swipeGestureDetector);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progressBar);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            systemName = bundle.getString("SystemName");

            DatabaseHelper db = new DatabaseHelper(getActivity());
            ArrayList<ReportItem> listReport = db.getAllMessages(systemName);

            if(listReport != null && listReport.size() > 0) {
                databaseToUI(listReport);
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                getData(systemName);
            }
        }

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void databaseToUI(ArrayList<ReportItem> listReport) {
        try {
            reportAdapter = new ReportAdapter(getActivity(), listReport);
            recyclerView.setAdapter(reportAdapter);
            recyclerView.setOnTouchListener((v, event) -> {
                gestureDetectorCompat.onTouchEvent(event);
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void safeData(HashMap data) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        db.setMessages(systemName, data);
    }

    private void getData(String sysname) {
        addMessage(sysname)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Exception e = task.getException();
                        Log.w("TAG", "addMessage:onFailure", e);
                        Toast.makeText(requireActivity().getApplicationContext(), "An error occurred."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HashMap result = task.getResult();
                    assert result != null;
                    showData(result);
                    safeData(result);
                });
    }

    private Task<HashMap> addMessage(String sysname) {
        DatabaseHelper db = new DatabaseHelper(getActivity());

        HashMap systemDetails = db.getSystemDetails(sysname);
        String idAnlagen = Objects.requireNonNull(systemDetails.get("idAnlagen")).toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("fk_anlagen", idAnlagen);

        return mFunctions
                .getHttpsCallable("getMeldung")
                .call(data)
                .continueWith(task -> (HashMap) Objects.requireNonNull(task.getResult()).getData());
    }

    private void showData(HashMap<String, List> data){
        try {
            List<HashMap> listdata = data.get("allmeldungen");

            assert listdata != null;
            for (int i = 0; i < listdata.size(); i++){
                HashMap<String, String> meldungen = listdata.get(i);

                String datum = meldungen.get("datum");
                String bemerkungMel = meldungen.get("bemerkungMel");
                String fk_meldungstyp = String.valueOf(meldungen.get("fk_meldungstyp"));

                arrayList.add(new ReportItem(datum, fk_meldungstyp, bemerkungMel));
            }
            progressBar.setVisibility(View.GONE);
            reportAdapter = new ReportAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(reportAdapter);
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }
}

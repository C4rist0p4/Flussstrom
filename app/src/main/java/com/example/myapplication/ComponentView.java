package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ComponentView extends Fragment {
    private FirebaseFunctions mFunctions;
    private LineGraphSeries<DataPoint> series;
    private GraphView graphView;

    public ComponentView() {
    }

    private static ComponentView newInstance(String param1, String param2) {
        ComponentView fragment = new ComponentView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_component_view, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        graphView = view.findViewById(R.id.graph);

        String systemName = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            systemName = bundle.getString("SystemName");
        }

        DatabaseHelper db = new DatabaseHelper(getActivity());
        HashMap listMeasuring = db.getMeasuring(systemName);

        if(listMeasuring != null && listMeasuring.size() > 0) {
            showMeasuring(listMeasuring);
        }
        else {
            getData(systemName);
        }
    }

    private void getData(String sysName) {
        addMessage(sysName)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("TAG", "getMeasuring:onFailure", e);
                        Toast.makeText(requireActivity().getApplicationContext(), "An error occurred."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HashMap result = task.getResult();
                    assert result != null;
                    safeData(sysName ,result);
                    //safeData(result);
                });
    }

    private Task<HashMap> addMessage(String sysName) {
        DatabaseHelper db = new DatabaseHelper(getActivity());

        HashMap systemDetails = db.getSystemDetails(sysName);
        String idAnlagen = Objects.requireNonNull(systemDetails.get("idAnlagen")).toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("idAnlage", idAnlagen);

        return mFunctions
                .getHttpsCallable("getMeasuring")
                .call(data)
                .continueWith(task -> (HashMap) Objects.requireNonNull(task.getResult()).getData());
    }
    private void safeData(String systemName, HashMap data) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        db.setMeasuring(systemName, data);
        //showMeasuring(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showMeasuring(HashMap<LocalTime, Double> listMeasuring) {
        try {
            series = new LineGraphSeries<>();
            List<LocalTime> keys = new ArrayList<>(listMeasuring.keySet());
            Collections.sort(keys);

            for(LocalTime key : keys) {
                double measuring = listMeasuring.get(key);
                series.appendData(new DataPoint(key.getMinute(), measuring), true, 15);
            }
            graphView.addSeries(series);

        }catch (Exception e){
            Log.d("Error", "showMeasuring" + e);
        }
    }
}

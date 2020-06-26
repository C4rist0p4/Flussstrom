package com.example.myapplication.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.database.entiy.Meldungen;

import com.example.myapplication.swipeGesture.SwipeGestureDetector;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Report extends Fragment {
    private FirebaseFunctions mFunctions;
    private RecyclerView recyclerView;

    private String systemName = null;
    ProgressBar progressBar;
    private SwipeGestureDetector swipeGestureDetector;
    private GestureDetectorCompat gestureDetectorCompat;
    private ReportViewModel reportViewModel;
    private Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();

        swipeGestureDetector = new SwipeGestureDetector(() -> {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.clearAnimation();
            getData(systemName);
        });
        gestureDetectorCompat = new GestureDetectorCompat(getActivity(), swipeGestureDetector);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progressBar);
        gson = new Gson();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            systemName = bundle.getString("SystemName");

            final ReportAdapter reportAdapter = new ReportAdapter();
            recyclerView.setAdapter(reportAdapter);

            reportViewModel = new ViewModelProvider(requireActivity(), new ReportViewModelFactory(getActivity().getApplication(), systemName)).get(ReportViewModel.class);
            reportViewModel.getAllbySystemName(systemName).observe(getActivity(), meldungen -> {
                reportAdapter.setMeldungen(meldungen);

                if(meldungen.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    getData(systemName);
                }
            });
        }
        return view;
    }

    private void getData(String sysname) {
        addMessage(sysname)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Exception e = task.getException();
                        Log.w("TAG", "addMessage:onFailure", e);
                        Toast.makeText(getActivity(), "An error occurred."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String result = task.getResult();
                    assert result != null;
                    safeData(result);
                });
    }

    private Task<String> addMessage(String sysname) {
        DatabaseHelper db = new DatabaseHelper(getActivity());

        HashMap systemDetails = db.getSystemDetails(sysname);
        String idAnlagen = Objects.requireNonNull(systemDetails.get("idAnlagen")).toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("fk_anlagen", idAnlagen);

        return mFunctions
                .getHttpsCallable("getMeldung")
                .call(data)
                .continueWith(task -> gson.toJson(Objects.requireNonNull(task.getResult()).getData()));
    }

    private void safeData(String data) {

        Meldungen[] meldungen = gson.fromJson(data, Meldungen[].class);

        try {
            JSONArray jArray = new JSONArray(data);

            for (Meldungen value : meldungen) {

                JSONObject jsonObject = jArray.getJSONObject(0);
                JSONObject jArray2 = (JSONObject) jsonObject.get("fk_meldungstyp");

                value.setMeldungstyp(jArray2.getString("bemerkungMT"));
                value.setSystemName(systemName);

                reportViewModel.insert(value);
            }
        } catch (JSONException e) {
            progressBar.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}

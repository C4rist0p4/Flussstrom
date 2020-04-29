package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComponentView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComponentView extends Fragment {
    private FirebaseFunctions mFunctions;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComponentView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComponentView.
     */
    // TODO: Rename and change types and number of parameters
    private static ComponentView newInstance(String param1, String param2) {
        ComponentView fragment = new ComponentView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String systemName = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            systemName = bundle.getString("SystemName");
        }

        getData(systemName);

/*        DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList<ReportItem> listReport = db.getAllMessages(systemName);

        if(listReport != null && listReport.size() > 0) {
            databaseToUI(listReport);
        }
        else {

        }*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_component_view, container, false);
    }

    private void getData(String sysName) {
        addMessage(sysName)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("TAG", "getMeasuring:onFailure", e);
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "An error occurred."
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
    }

/*    private void showData(HashMap<JSONObject, ArrayList> data){
        try {
            ArrayList jarray = data.get("measuring");
            Log.d("TAG", "showData: "+ jarray.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SystemAdapter.OnSystemListener {
    //private FirebaseAuth mAuth;
    private Intent intent;
    private RecyclerView recyclerView;
    private FirebaseFunctions mFunctions;
    ArrayList<SystemItem> listSystem;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.include2);
        setSupportActionBar(myToolbar);

        mFunctions = FirebaseFunctions.getInstance();

        recyclerView = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db = new DatabaseHelper(this);

        if(db.countSystemes() == 0){
             getSystem();
        }
        else {
            listSystem = db.getSystemItem();
            showSystems(listSystem);
        }

        /*
        mAuth = FirebaseAuth.getInstance();

        TextView flottilleTV = findViewById(R.id.flottilleTV);
        flottilleTV.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        });*/
    }

    private void showSystems(ArrayList<SystemItem> listSystemes) {
        SystemAdapter systemAdapter = new SystemAdapter(this, listSystemes, this);
        recyclerView.setAdapter(systemAdapter);
    }

    private void showSystemsFromCall(ArrayList<HashMap> mapSystem) {
        ArrayList<SystemItem> systemList = new ArrayList<>();

        for (HashMap System : mapSystem){
            String name = Objects.requireNonNull(System.get("anlagenname")).toString();
            SystemItem systemItem = new SystemItem(name, "2", "2");
            systemList.add(systemItem);
            listSystem = systemList;
        }

        SystemAdapter systemAdapter = new SystemAdapter(this, listSystem, this);
        recyclerView.setAdapter(systemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.LogOut) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        startActivity(intent);
        finish();
    }

    @Override
    public void onSystemClick(int position) {
        String name = listSystem.get(position).getName();
        intent = new Intent(this, MachineryActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private void getSystem() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<String> SystemId = db.getSystemId();

        getMachineryData(SystemId);
    }

    private void getMachineryData(List<String> SystemId) {
        addMessage(SystemId)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("TAG", "addMessage:onFailure", e);
                        Toast.makeText(Objects.requireNonNull(this).getApplicationContext(), "An error occurred."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList result = task.getResult();
                    assert result != null;

                    showSystemsFromCall(result);
                    safeData(result);
                });
    }

    private Task<ArrayList> addMessage(List<String> systemId) {

        HashMap<String, Object> data = new HashMap<>();
        ArrayList<HashMap<String, String>> idList = new ArrayList<>();
        
        for(String id : systemId){
            HashMap<String, String> jid = new HashMap<>();
            jid.put("id", id);
            idList.add(jid);
        }
        data.put("idAnlagen", idList);

        return mFunctions
                .getHttpsCallable("getMachinery")
                .call(data)
                .continueWith(task -> (ArrayList) Objects.requireNonNull(task.getResult()).getData());

    }

    private void safeData(ArrayList machinery) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.addMasterData(machinery);
    }
}

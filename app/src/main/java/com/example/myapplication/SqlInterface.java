package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Objects;


public class SqlInterface extends AppCompatActivity {
    private FirebaseFunctions mFunctions;
    private TextView dataTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_interface);
        dataTv = findViewById(R.id.dataEt);
        mFunctions = FirebaseFunctions.getInstance();
    }

    private Task<String> addMessage() {

        return mFunctions
                .getHttpsCallable("getMeldung")
                .call()
                .continueWith(task -> {
                    HashMap<String, String> result;
                    try {
                        result = (HashMap<String, String>) Objects.requireNonNull(task.getResult()).getData();
                        assert result != null;
                        return result.toString();
                    } catch (Exception e) {
                        throw new Exception(e);
                    }
                });
    }

    public void getSqlQuery(View view) {
        addMessage()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        if (e instanceof FirebaseFunctionsException) {
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                            Object details = ffe.getDetails();
                        }
                            // [START_EXCLUDE]
                            Log.w("addMessage:onFailure", e);
                            showSnackbar("An error occurred.");
                            return;
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE]
                        String result = task.getResult();

                        dataTv.setText(result);
                        // [END_EXCLUDE]
                    });
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}

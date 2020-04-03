package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFunctions mFunctions;

    private TextView emailET;
    private TextView passwordET;
    private TextView nameET;
    private Intent intent;

    private String email;
    private String name;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.password1ET);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            updateUI();
        }
    }

    public void signInWithEmailAndPassword(View view) {
         email = emailET.getText().toString();
         password = passwordET.getText().toString();
         name = nameET.getText().toString();
         //TODO Passwort auf 6 zeichen prüfen

         callHttpCloudFunction();
    }

    public void createUser(View view) {
        intent = new Intent(this, CreateUser.class);
        startActivity(intent);
    }

    private void callHttpCloudFunction() {
        callCloudFunction()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("TAG", "addMessage:onFailure", e);
                        return;
                    }
                    String result = task.getResult();
                    assert result != null;
                    if (Boolean.parseBoolean(result)) {
                        signIn();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Task<String> callCloudFunction() {
        mFunctions = FirebaseFunctions.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("password", password);

        return mFunctions
                .getHttpsCallable("checkUsers")
                .call(data)
                .continueWith(task -> (String) Objects.requireNonNull(task.getResult()).getData());
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("success", "signInWithEmail:success");
                    updateUI();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Create new User", "createUserWithEmail", task.getException());
                    createUser();
                }
            });}

    private void updateUI() {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("success", "createUserWithEmail:success");
                        // TODO
                        updateUI();
                    } else {
                        Log.w("failure", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

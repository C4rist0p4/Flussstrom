package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.pictureDownload.LoadingDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFunctions mFunctions;

    private TextView emailET;
    private TextView passwordET;
    private TextView nameET;
    private Intent intent;

    private String email;
    private  String name;
    private String password;

    private DatabaseHelper db;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.password1ET);

        loadingDialog = new LoadingDialog(LoginActivity.this);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            startMainActvity();
        }
    }

    public void signInWithEmailAndPassword(View view) {
         email = emailET.getText().toString();
         password = passwordET.getText().toString();
         name = nameET.getText().toString();

        if (password.length() <= 5) {
            Toast.makeText(LoginActivity.this,
                    "Ihr Passwort muss mindestens 6 Zeichen lang sein.",
                    Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("password", password);
            loadingDialog.startLoadingDialog();

            callHttpCloudFunction("checkUsers", data);
        }
    }

    private void authenticationWithFirebase() {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("success", "signInWithEmail:success");
                    getUserData(name);

                } else {
                    //Create New Useser in Firebase
                    Log.w("Create new User", "createUserWithEmail", task.getException());
                    addUserToFirebase();
                }
            });
    }

    private void startMainActvity() {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openCreateUser(View view) {
        intent = new Intent(this, CreateUser.class);
        startActivity(intent);
    }

    private void checkSystemId() {
        db = new DatabaseHelper(this);
        List<String> systemId = db.getSystemId();

        if (systemId == null || systemId.size() == 0) {
            getUserData("name");
        }
    }

    public void addUserToFirebase() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i("success", "createUserWithEmail success");
                        setTokensToMariaDB(name);
                    } else {
                        loadingDialog.dismissDialog();
                        Log.w("failure", "createUserWithEmail failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Create User failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setTokensToMariaDB(String n) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task1 -> {
                    if (!task1.isSuccessful()) {
                        loadingDialog.dismissDialog();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Log.w("TAG", "getInstanceId failed", task1.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task1.getResult()).getToken();
                    HashMap<String, Object> data = new HashMap<>();

                    data.put("name", n);
                    data.put("idDevice", token);

                    callHttpCloudFunction("setIdDevice", data);
                });
    }

    private void callHttpCloudFunction(String funcName, HashMap data) {
        callCloudFunction(funcName, data)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.w("HttpFailure", "Failure on "+ funcName , e);
                        return;
                    }

                    HashMap result = task.getResult();
                    assert result != null;
                        if (Objects.equals(result.get("message"), "true")) {
                            authenticationWithFirebase();
                        } else if(Objects.equals(result.get("message"), "Id Device set")) {
                            Log.i("Device", "Id set");
                            getUserData(name);
                        } else if(Objects.equals(result.get("message"), "Userdata")) {
                            Log.i("Userdate", "receive Userdata");
                            safeUserID(result);
                        } else {
                            loadingDialog.dismissDialog();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(LoginActivity.this, "Authentication failed " + result,
                                    Toast.LENGTH_SHORT).show();
                        }
                });
    }

    private Task<HashMap> callCloudFunction(String funcName, HashMap data) {
        mFunctions = FirebaseFunctions.getInstance();
        return mFunctions
                .getHttpsCallable(funcName)
                .call(data)
                .continueWith(task -> (HashMap) Objects.requireNonNull(task.getResult()).getData());
    }

    private void getUserData(String n) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", n);

        callHttpCloudFunction("getUserData", data);
    }

    private void safeUserID(HashMap data) {
        db = new DatabaseHelper(this);
        db.addUser(data);
        db.addSystemId(data);

        startMainActvity();
    }
}

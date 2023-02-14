package com.example.mobileprogramming_assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userID;
    int progress;
    Button btnConfirm;
    ProgressDialog progressDialog;
    EditText inputName, inputPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        assert mUser != null;

        userID = mUser.getUid();
        inputName = findViewById(R.id.inputName);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);

        Log.d("Data", "userID: " + userID + "name: " + inputName + "progress: " + progress);
        btnConfirm.setOnClickListener(v -> PerformAuth());
    }

    private void PerformAuth() {
        String name = inputName.getText().toString();
        String phoneNumber = inputPhoneNumber.getText().toString();

        if (name.isEmpty()) {
            inputName.setError("Please insert correct Name");
        } else if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            inputPhoneNumber.setError("Please insert correct Phone Number");
        } else {
            progressDialog.setMessage("Updating Profile...");
            progressDialog.setTitle("User Personal Info");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            progressDialog.dismiss();


        }
    }
}

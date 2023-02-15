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

import com.google.firebase.firestore.FirebaseFirestore;

public class VerifyActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userID, name, phoneNumber, email;


    int progress;
    Button btnConfirm;
    ProgressDialog progressDialog;
    EditText inputName, inputPhoneNumber;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        assert mUser != null;

        userID = mUser.getUid();

        email = mUser.getEmail();

        inputName = findViewById(R.id.inputName);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        btnConfirm = findViewById(R.id.btnConfirm);
        progressDialog = new ProgressDialog(this);
        btnConfirm.setOnClickListener(v -> AddUserData());
    }

    private void AddUserData() {
        name = inputName.getText().toString();
        phoneNumber = inputPhoneNumber.getText().toString();

        if (name.isEmpty()) {
            inputName.setError("Please insert correct Name");
        } else if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            inputPhoneNumber.setError("Please insert correct Phone Number");
        } else {
            progressDialog.setMessage("Updating Profile...");
            progressDialog.setTitle("User Personal Info");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            UserInfo newUser = new UserInfo(userID, name, phoneNumber, email, 0);
            db.collection("user").document(userID).set(newUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    Intent intent = new Intent(VerifyActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Toast.makeText(VerifyActivity.this, "Profile Update Successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(VerifyActivity.this, "Invalid Name or Phone Number", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

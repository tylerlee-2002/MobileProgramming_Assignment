package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    String userID, name, phoneNumber, email;

    TextView txtName, txtEmail, txtPhoneNumber;

    EditText inputPhoneNumber;

    Button btnConfirm;

    ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        email = getIntent().getStringExtra("email");

        txtName = findViewById(R.id.txtName);
        txtName.setText(String.format("Name: %s", name));

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(String.format("Email: %s", email));

        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtPhoneNumber.setText(String.format("Tel: %s", phoneNumber));

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            String phoneNumber = inputPhoneNumber.getText().toString();

            if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
                inputPhoneNumber.setError("Enter Correct Phone Number");
            } else {
                Map<String, Object> updates2 = new HashMap<>();
                updates2.put("phoneNumber", phoneNumber);
                FirebaseFirestore.getInstance().collection("user").document(userID).update(updates2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Toast.makeText(ProfileActivity.this, "New Phone Number Updated!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error updating document!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
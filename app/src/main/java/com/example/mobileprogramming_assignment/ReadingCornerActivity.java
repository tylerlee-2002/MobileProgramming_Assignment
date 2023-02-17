package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ReadingCornerActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnNext, btnPrevious, btnBackHome;
    TextView txtPage1;
    int progress;

    String userID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_corner);

        userID = getIntent().getStringExtra("userID");
        progress = getIntent().getIntExtra("progress", progress);

        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnBackHome = findViewById(R.id.btnBackHome);

        txtPage1 = findViewById(R.id.txtPage1);

        String[] topic = {"1", "2", "3", "4", "5"};
        txtPage1.setText(topic[progress]);

        btnNext.setOnClickListener(v -> {
            if (progress>=0 && progress < 4) {
                progress = progress + 1;
                txtPage1.setText(topic[progress]);

                Map<String, Object> updates = new HashMap<>();
                updates.put("progress", progress);
                db.collection("user").document(userID).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Progress started!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating user data", e);
                    }
                });

            } else {
                Log.d("New Page", "New Page");
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (progress > 0) {
                progress = progress -1;
                txtPage1.setText(topic[progress]);

                Map<String, Object> updates = new HashMap<>();
                updates.put("progress", progress);
                db.collection("user").document(userID).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Progress started!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating user data", e);
                    }
                });

                if (progress == 0) {
                    btnPrevious.setText(R.string.back_to_home);
                }
            } else {
                Intent intent = new Intent(ReadingCornerActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


        });

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(ReadingCornerActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}

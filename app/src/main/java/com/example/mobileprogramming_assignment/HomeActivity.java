package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mobileprogramming_assignment.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnLogout;
    String email, userID;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        assert mUser != null;
        email = mUser.getEmail();
        userID = mUser.getUid();

        db = FirebaseFirestore.getInstance();
        db.collection("user").whereEqualTo("uid", userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if (task.getResult().isEmpty()){
                    Log.d("Works?", "yes");
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("userId", userID);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        final TextView helloTextView = findViewById(R.id.txtWelcome);
        helloTextView.setText(String.format("Welcome %s!", email));

        // Function for logout button
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
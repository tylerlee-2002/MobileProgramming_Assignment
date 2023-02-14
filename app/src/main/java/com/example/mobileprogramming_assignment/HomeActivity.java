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

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnLogout;
    String email, userID, name;

    int progress;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        assert mUser != null;

        db = FirebaseFirestore.getInstance();
        db.collection("user").whereEqualTo("uid", mUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if (task.getResult().isEmpty()){
                    userID = mUser.getUid();
                    name = mUser.getPhoneNumber();
                    progress = 0;
                    Log.d("Data", "userID: " + userID + "name: " + name + "progress: " + progress);

                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        userID = Objects.requireNonNull(document.getData().get("uid")).toString();
                        name = Objects.requireNonNull(document.getData().get("name")).toString();
                        progress = Integer.parseInt(Objects.requireNonNull(document.getData().get("progress")).toString());

                        Log.d("Data", "userID: " + userID + "name: " + name + "progress: " + progress);
                    }
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        final TextView helloTextView = findViewById(R.id.txtWelcome);
        helloTextView.setText(String.format("Welcome %s!", name));

        // Function for logout button
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
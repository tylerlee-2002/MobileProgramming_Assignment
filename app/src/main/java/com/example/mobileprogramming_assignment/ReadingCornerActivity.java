package com.example.mobileprogramming_assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReadingCornerActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnNext, btnPrevious, btnBackHome;
    TextView txtPage1;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_corner);

        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnBackHome = findViewById(R.id.btnBackHome);

        txtPage1 = findViewById(R.id.txtPage1);

        String[] topic = {"Dementia Syndrome \n 1)xxxxxxxx \n 2)xxxxxxxx \n", "Tips for communication with dementia person \n 1)xxxxxxxx \n 2)xxxxxxxx \n"};
        txtPage1.setText(topic[progress]);

        btnNext.setOnClickListener(v -> {
            if (progress < 2) {
                progress = progress + 1;
                txtPage1.setText(topic[progress]);
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (progress > 0) {
                progress = progress -1;
                txtPage1.setText(topic[progress]);
            }
        });

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(ReadingCornerActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}

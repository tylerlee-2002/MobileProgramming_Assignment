package com.example.mobileprogramming_assignment;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class CertActivity extends AppCompatActivity {

    Button btnBackHome, btnDownload;
    TextView txtUsername;
    String userID, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cert);

        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");

        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CertActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

//                TextView textViewCurrentProgress = findViewById(R.id.txtCurrentProgress);
//                textViewCurrentProgress.setText(String.format("Current Progress %s / 5!", userID.completeUntil()));

            }
        });

        txtUsername = findViewById(R.id.txtUsername);
        txtUsername.setText(name);

        btnDownload = findViewById(R.id.btnDownload);
    }
}

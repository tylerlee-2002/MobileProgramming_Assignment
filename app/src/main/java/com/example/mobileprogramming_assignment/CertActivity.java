package com.example.mobileprogramming_assignment;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class CertActivity extends AppCompatActivity {

    FirebaseUser mUser;
    Button btnBack, btnDownload;
    String userID, name, email, phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cert);

    }
}

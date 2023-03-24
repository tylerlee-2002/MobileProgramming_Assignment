package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ExamActivity extends AppCompatActivity {

    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int q1Answer = 0, q2Answer = 0, q3Answer = 0, q4Answer = 0, q5Answer = 0, totalMark = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;

        ImageButton btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setMessage("Are you sure you want to exit exam?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Perform the action when the "Yes" button is clicked
                    Intent intent = new Intent(ExamActivity.this, CourseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Perform the action when the "No" button is clicked
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        RadioGroup radioGroup1 = findViewById(R.id.radio_group);
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button1:
                    q1Answer = 1;
                    break;
                case R.id.radio_button2:
                    q1Answer = 2;
                    break;
                case R.id.radio_button3:
                    q1Answer = 3;
                    break;
            }
        });

        RadioGroup radioGroup2 = findViewById(R.id.radio_group2);
        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button4:
                    q2Answer = 1;
                    break;
                case R.id.radio_button5:
                    q2Answer = 2;
                    break;
                case R.id.radio_button6:
                    q2Answer = 3;
                    break;
            }
        });

        RadioGroup radioGroup3 = findViewById(R.id.radio_group3);
        radioGroup3.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button7:
                    q3Answer = 1;
                    break;
                case R.id.radio_button8:
                    q3Answer = 2;
                    break;
                case R.id.radio_button9:
                    q3Answer = 3;
                    break;
            }
        });

        RadioGroup radioGroup4 = findViewById(R.id.radio_group4);
        radioGroup4.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button10:
                    q4Answer = 1;
                    break;
                case R.id.radio_button11:
                    q4Answer = 2;
                    break;
                case R.id.radio_button12:
                    q4Answer = 3;
                    break;
            }
        });

        RadioGroup radioGroup5 = findViewById(R.id.radio_group5);
        radioGroup5.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button13:
                    q5Answer = 1;
                    break;
                case R.id.radio_button14:
                    q5Answer = 2;
                    break;
                case R.id.radio_button15:
                    q5Answer = 3;
                    break;
            }
        });

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            // Check whether user input all answer, if not pop up alert msg.
            if (q1Answer == 0 || q2Answer == 0 || q3Answer == 0 || q4Answer == 0 || q5Answer == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("Please fill in each questions!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                if(q1Answer == 3){
                    totalMark += 20;
                }

                if(q2Answer == 2){
                    totalMark += 20;
                }

                if(q3Answer == 2){
                    totalMark += 20;
                }

                if(q4Answer == 2){
                    totalMark += 20;
                }

                if(q5Answer == 1){
                    totalMark += 20;
                }

                Map<String, Object> updates = new HashMap<>();
                updates.put("examDone", true);
                updates.put("mark", totalMark);
                updates.put("q1Answer", q1Answer);
                updates.put("q2Answer", q2Answer);
                updates.put("q3Answer", q3Answer);
                updates.put("q4Answer", q4Answer);
                updates.put("q5Answer", q5Answer);

                db.collection("user").document(mUser.getUid()).update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Exam Submitted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating Exam data", e);
                        }
                    });

                // Exam ended, back to Course!
                Intent intent = new Intent(ExamActivity.this, CourseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
            }
        });
    }
}
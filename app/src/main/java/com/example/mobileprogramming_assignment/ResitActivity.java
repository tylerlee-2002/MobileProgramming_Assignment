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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResitActivity extends AppCompatActivity {

    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int q1Answer = 0, q2Answer = 0, q3Answer = 0, q4Answer = 0, q5Answer = 0, mark = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;

        RadioButton radioButton1 = findViewById(R.id.radio_button1);
        RadioButton radioButton2 = findViewById(R.id.radio_button2);
        RadioButton radioButton3 = findViewById(R.id.radio_button3);
        RadioButton radioButton4 = findViewById(R.id.radio_button4);
        RadioButton radioButton5 = findViewById(R.id.radio_button5);
        RadioButton radioButton6 = findViewById(R.id.radio_button6);
        RadioButton radioButton7 = findViewById(R.id.radio_button7);
        RadioButton radioButton8 = findViewById(R.id.radio_button8);
        RadioButton radioButton9 = findViewById(R.id.radio_button9);
        RadioButton radioButton10 = findViewById(R.id.radio_button10);
        RadioButton radioButton11 = findViewById(R.id.radio_button11);
        RadioButton radioButton12 = findViewById(R.id.radio_button12);
        RadioButton radioButton13 = findViewById(R.id.radio_button13);
        RadioButton radioButton14 = findViewById(R.id.radio_button14);
        RadioButton radioButton15 = findViewById(R.id.radio_button15);

        radioButton1.setEnabled(false);
        radioButton2.setEnabled(false);
        radioButton3.setEnabled(false);
        radioButton4.setEnabled(false);
        radioButton5.setEnabled(false);
        radioButton6.setEnabled(false);
        radioButton7.setEnabled(false);
        radioButton8.setEnabled(false);
        radioButton9.setEnabled(false);
        radioButton10.setEnabled(false);
        radioButton11.setEnabled(false);
        radioButton12.setEnabled(false);
        radioButton13.setEnabled(false);
        radioButton14.setEnabled(false);
        radioButton15.setEnabled(false);

        radioButton3.setBackgroundResource(R.drawable.border_green);
        radioButton5.setBackgroundResource(R.drawable.border_green);
        radioButton8.setBackgroundResource(R.drawable.border_green);
        radioButton11.setBackgroundResource(R.drawable.border_green);
        radioButton13.setBackgroundResource(R.drawable.border_green);

        db.collection("user").whereEqualTo("uid", mUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    mark = Integer.parseInt(Objects.requireNonNull(document.getData().get("mark")).toString());
                    q1Answer = Integer.parseInt(Objects.requireNonNull(document.getData().get("q1Answer")).toString());
                    q2Answer = Integer.parseInt(Objects.requireNonNull(document.getData().get("q2Answer")).toString());
                    q3Answer = Integer.parseInt(Objects.requireNonNull(document.getData().get("q3Answer")).toString());
                    q4Answer = Integer.parseInt(Objects.requireNonNull(document.getData().get("q4Answer")).toString());
                    q5Answer = Integer.parseInt(Objects.requireNonNull(document.getData().get("q5Answer")).toString());
                }

                TextView txtheader = findViewById(R.id.txtheader);
                txtheader.setText("Preview: Exam Result");

                TextView txtheaderDesc = findViewById(R.id.txtheaderDesc);
                txtheaderDesc.setText(String.format("Total Marks: %s/100 %s", mark, "%"));

                if (q1Answer == 1){
                    radioButton1.setChecked(true);
                    radioButton1.setBackgroundResource(R.drawable.border_red);} else if (q1Answer == 2){
                    radioButton2.setChecked(true);
                    radioButton2.setBackgroundResource(R.drawable.border_red);} else if (q1Answer == 3){
                    radioButton3.setChecked(true);
                }

                if (q2Answer == 1){
                    radioButton4.setChecked(true);
                    radioButton4.setBackgroundResource(R.drawable.border_red);
                } else if (q2Answer == 2){
                    radioButton5.setChecked(true);
                } else if (q2Answer == 3){
                    radioButton6.setChecked(true);
                    radioButton6.setBackgroundResource(R.drawable.border_red);
                }

                if (q3Answer == 1){
                    radioButton7.setChecked(true);
                    radioButton7.setBackgroundResource(R.drawable.border_red);
                } else if (q3Answer == 2){
                    radioButton8.setChecked(true);
                } else if (q3Answer == 3){
                    radioButton9.setChecked(true);
                    radioButton9.setBackgroundResource(R.drawable.border_red);
                }

                if (q4Answer == 1){
                    radioButton10.setChecked(true);
                    radioButton10.setBackgroundResource(R.drawable.border_red);
                } else if (q4Answer == 2){
                    radioButton11.setChecked(true);
                } else if (q4Answer == 3){
                    radioButton12.setChecked(true);
                    radioButton12.setBackgroundResource(R.drawable.border_red);
                }

                if (q5Answer == 1){
                    radioButton13.setChecked(true);
                } else if (q5Answer == 2){
                    radioButton14.setChecked(true);
                    radioButton14.setBackgroundResource(R.drawable.border_red);
                } else if (q5Answer == 3){
                    radioButton15.setChecked(true);
                    radioButton15.setBackgroundResource(R.drawable.border_red);
                }
            }
        });

        ImageButton btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setMessage("Exit exam preview?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Perform the action when the "Yes" button is clicked
                    Intent intent = new Intent(ResitActivity.this, CourseActivity.class);
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

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setText("Retake Exam");
        submitButton.setOnClickListener(v -> {
            if (mark >= 60) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("Previous Mark will be affected. Are you sure you want to resit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform the action when the "Yes" button is clicked
                        Intent intent = new Intent(ResitActivity.this, ExamActivity.class);
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
            } else {
                // Preview Ended, back to Exam (Retake)!
                Intent intent = new Intent(ResitActivity.this, ExamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
            }
        });
    }
}
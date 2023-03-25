package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReadingCornerActivity extends AppCompatActivity {

    Button btnNext, btnPrevious, btnBackHome;

    ImageView imageView1;

    int progress, completeUntil;
    String userID, name, phoneNumber, email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textViewCurrentProgress;
//
//    int[] topics = {R.drawable.topic_1, R.drawable.topic_2, R.drawable.topic_3, R.drawable.topic_4, R.drawable.topic_5};
//
//    String[] quiz =
//            {
//                    "What will become of the cognitive Changes? ",
//                    "What will become the case with psychological changes?",
//                    "Tips for communicating (Encouraging)",
//                    "Tips for communicating (Body Language)",
//                    "How to deal with Troubling behaviour Person"
//            };
//
//    String[] option1 =
//            {
//                    "Confusion and disorientation",
//                    "Appropriate behavior",
//                    "Speak clearly and slowly",
//                    "Don't be too deferential when talking to them",
//                    "Ask a GP for an assessment"
//            };
//
//    String[] option2 =
//            {
//                    "Handle tasks with ease",
//                    "Depression",
//                    "Communicate out of the other person's line of sight",
//                    "Keeping a negative and unfriendly tone",
//                    "Do nothing"
//            };
//
//    String[] option3 =
//            {
//                    "Communicating easily with each other",
//                    "Good Personality",
//                    "Avoid joining others' conversations",
//                    "Be patient and remain calm",
//                    "Just ignored"
//            };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reading_corner);
//
//        userID = getIntent().getStringExtra("userID");
//        name = getIntent().getStringExtra("name");
//        phoneNumber = getIntent().getStringExtra("phoneNumber");
//        email = getIntent().getStringExtra("email");
//        completeUntil = getIntent().getIntExtra("completeUntil", completeUntil);
//
//        if (completeUntil == 5) {
//            progress = 0;
//        } else {
//            progress = completeUntil;
//        }
//
//        textViewCurrentProgress = findViewById(R.id.txtCurrentProgress);
//        textViewCurrentProgress.setText(String.format("Dementia Topic %s/5", progress + 1));
//
//        imageView1 = findViewById(R.id.imageView);
//        imageView1.setImageResource(topics[progress]);
//
//        btnNext = findViewById(R.id.btnNext);
//        btnNext.setOnClickListener(v -> showPopupWindow());
//
//        btnPrevious = findViewById(R.id.btnPrevious);
//        btnPrevious.setOnClickListener(v -> {
//            if (progress > 0) {
//                progress = progress - 1;
//                imageView1.setImageResource(topics[progress]);
//                if (progress == 0) {
//                    btnPrevious.setText(R.string.back_to_home);
//                } else {
//                    btnPrevious.setText(R.string.previous);
//                }
//                textViewCurrentProgress.setText(String.format("Dementia Topic %s/5", progress + 1));
//            } else {
//                Intent intent = new Intent(ReadingCornerActivity.this, HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
//
//        btnBackHome = findViewById(R.id.btnBackHome);
//        btnBackHome.setOnClickListener(v -> {
//            Intent intent = new Intent(ReadingCornerActivity.this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        });
//    }
//
//    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
//    private void showPopupWindow() {
//
//        // inflate the layout of the popup window
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_quiz_window, null);
//
//        // create the popup window
//        int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; // lets taps outside the popup also dismiss it
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//        TextView myTextView = popupView.findViewById(R.id.textView);
//        myTextView.setText(quiz[progress]);
//
//        RadioGroup radioGroup = popupView.findViewById(R.id.radio_group);
//        RadioButton RadioButton1 = popupView.findViewById(R.id.radio_button1);
//        RadioButton RadioButton2 = popupView.findViewById(R.id.radio_button2);
//        RadioButton RadioButton3 = popupView.findViewById(R.id.radio_button3);
//
//        RadioButton1.setText(option1[progress]);
//        RadioButton2.setText(option2[progress]);
//        RadioButton3.setText(option3[progress]);
//
//        Button closeButton = popupView.findViewById(R.id.closeButton);
//        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            switch (checkedId) {
//                case R.id.radio_button1:
//                    if (progress == 0 || progress == 2 || progress == 4) {
//                        closeButton.setText(R.string.correctAnswer);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.teal_200));
//                        closeButton.setEnabled(true);
//                    } else if (progress == 1) {
//                        closeButton.setText(R.string.wrongAnswer);
//                        closeButton.setTextColor(0xeb3434);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
//                    } else if (progress == 3) {
//                        closeButton.setText(R.string.tryAgain);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
//                        closeButton.setEnabled(false);
//                    }
//                    break;
//                case R.id.radio_button2:
////                        progress = 1
//                    // Code to handle option 2 selection
//                    if (progress == 0 || progress == 2 || progress == 4) {
//                        closeButton.setText(R.string.tryAgain);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
//                        closeButton.setEnabled(false);
//                    } else if (progress == 1) {
//                        closeButton.setText(R.string.correctAnswer);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.teal_200));
//                        closeButton.setEnabled(true);
//                    } else if (progress == 3) {
//                        closeButton.setText(R.string.wrongAnswer);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
//                        closeButton.setEnabled(false);
//                    }
//                    break;
//                case R.id.radio_button3:
//                    if (progress == 0 || progress == 2 || progress == 4) {
//                        closeButton.setText(R.string.wrongAnswer);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
//                        closeButton.setEnabled(false);
//                    } else if (progress == 1) {
//                        closeButton.setText(R.string.tryAgain);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
//                        closeButton.setEnabled(false);
//                    } else if (progress == 3) {
//                        closeButton.setText(R.string.correctAnswer);
//                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.teal_200));
//                        closeButton.setEnabled(true);
//                    }
//                    break;
//            }
//        });
//
//        // show the popup window
//        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//
//        closeButton.setOnClickListener(v -> {
//            // Only update if user first time passed the quiz.
//            progress = progress + 1;
//
//            if (completeUntil < progress) {
//                completeUntil = progress;
//                Map<String, Object> updates2 = new HashMap<>();
//                updates2.put("completeUntil", completeUntil);
//                db.collection("user").document(userID).update(updates2).addOnSuccessListener(aVoid -> Log.d(TAG, "Topic Completed!")).addOnFailureListener(e -> Log.e(TAG, "Error updating user data", e));
//            }
//
//            if (progress == 0) {
//                btnPrevious.setText(R.string.back_to_home);
//            } else {
//                btnPrevious.setText(R.string.previous);
//            }
//
//            if (progress == 5 && completeUntil == 5) {
//                textViewCurrentProgress.setText(String.format("Dementia Topic %s/5", progress));
//                Intent intent = new Intent(ReadingCornerActivity.this, CertActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("userID", userID);
//                intent.putExtra("name", name);
//                startActivity(intent);
//            } else {
//                textViewCurrentProgress.setText(String.format("Dementia Topic %s/5", progress + 1));
//                imageView1.setImageResource(topics[progress]);
//            }
//
//            popupWindow.dismiss();
//        });
//    }
}

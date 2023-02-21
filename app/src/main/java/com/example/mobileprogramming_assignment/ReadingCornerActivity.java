package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

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
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ReadingCornerActivity extends AppCompatActivity {

    Button btnNext, btnPrevious, btnBackHome;

    ImageView imageView1;

    int progress, completeUntil;
    String userID, name, phoneNumber, email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int[] topics = {R.drawable.topic_1, R.drawable.topic_2, R.drawable.topic_3, R.drawable.topic_4, R.drawable.topic_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_corner);

        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        email = getIntent().getStringExtra("email");
        completeUntil = getIntent().getIntExtra("completeUntil", completeUntil);

        if (completeUntil == 5) {
            progress = 4;
        } else {
            progress = completeUntil;
        }

        imageView1 = findViewById(R.id.imageView);
        imageView1.setImageResource(topics[progress]);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            showPopupWindow();
        });

        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(v -> {
            if (progress > 0) {
                progress = progress - 1;
                imageView1.setImageResource(topics[progress]);
                if (progress == 0) {
                    btnPrevious.setText(R.string.back_to_home);
                } else {
                    btnPrevious.setText(R.string.previous);
                }

            } else {
                Intent intent = new Intent(ReadingCornerActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(ReadingCornerActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void showPopupWindow() {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView myTextView = popupView.findViewById(R.id.textView);
        myTextView.setText(String.format("Quiz %d", progress + 1));

        RadioGroup radioGroup = popupView.findViewById(R.id.radio_group);

        Button closeButton = popupView.findViewById(R.id.closeButton);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button1:
                        if (progress == 0 || progress == 2 || progress == 3) {
                            closeButton.setText(R.string.correctAnswer);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.teal_200));
                            closeButton.setEnabled(true);
                        } else if (progress == 1) {
                            closeButton.setText(R.string.wrongAnswer);
                            closeButton.setTextColor(0xeb3434);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                        } else if (progress == 4) {
                            closeButton.setText(R.string.tryAgain);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                            closeButton.setEnabled(false);
                        }
                        break;
                    case R.id.radio_button2:
//                        progress = 1
                        // Code to handle option 2 selection
                        if (progress == 0 || progress == 2 || progress == 3) {
                            closeButton.setText(R.string.tryAgain);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                            closeButton.setEnabled(false);
                        } else if (progress == 1) {
                            closeButton.setText(R.string.correctAnswer);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.teal_200));
                            closeButton.setEnabled(true);
                        } else if (progress == 4) {
                            closeButton.setText(R.string.wrongAnswer);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                            closeButton.setEnabled(false);
                        }
                        break;
                    case R.id.radio_button3:
                        if (progress == 0 || progress == 2 || progress == 3) {
                            closeButton.setText(R.string.wrongAnswer);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                            closeButton.setEnabled(false);
                        } else if (progress == 1) {
                            closeButton.setText(R.string.tryAgain);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                            closeButton.setEnabled(false);
                        } else if (progress == 4) {
                            closeButton.setText(R.string.correctAnswer);
                            closeButton.setTextColor(getApplication().getResources().getColor(R.color.teal_200));
                            closeButton.setEnabled(true);
                        }
                        break;
                }
            }

        });

        // show the popup window
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only update if user first time passed the quiz.

                progress = progress + 1;

                if (completeUntil < progress){
                    completeUntil = progress;
                    Map<String, Object> updates2 = new HashMap<>();
                    updates2.put("completeUntil", completeUntil);
                    db.collection("user").document(userID).update(updates2).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Topic Completed!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error updating user data", e);
                        }
                    });
                }

                if (progress == 0) {
                    btnPrevious.setText(R.string.back_to_home);
                } else {
                    btnPrevious.setText(R.string.previous);
                }

                if (completeUntil >= 5) {
                    Intent intent = new Intent(ReadingCornerActivity.this, CertActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userID", userID);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } else {
                    imageView1.setImageResource(topics[progress]);
                }

                popupWindow.dismiss();
            }
        });
    }
}

package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseUser mUser;
    
    Button btnLogout, btnContinue, btnShare, btnCert;
    ImageButton btnProfile;
    String userID, name, email, phoneNumber;
    int completeUntil;
    ProgressBar progressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textViewCurrentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;

        db.collection("user").whereEqualTo("uid", mUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    userID = Objects.requireNonNull(document.getData().get("uid")).toString();
                    name = Objects.requireNonNull(document.getData().get("name")).toString();
                    phoneNumber = Objects.requireNonNull(document.getData().get("phoneNumber")).toString();
                    email = Objects.requireNonNull(document.getData().get("email")).toString();
                    completeUntil = Integer.parseInt(Objects.requireNonNull(document.getData().get("completeUntil")).toString());
                }

                UserInfo user = new UserInfo(userID, name, phoneNumber, email, completeUntil);

                final TextView helloTextView = findViewById(R.id.txtWelcome);
                helloTextView.setText(String.format("Welcome %s !", user.getName()));

                textViewCurrentProgress = findViewById(R.id.txtCurrentProgress);
                textViewCurrentProgress.setText(
                        String.format(
                                "Progress: %s / 5" +
                                        "\n Topic 1 : %s" +
                                        "\n Topic 2 : %s" +
                                        "\n Topic 3 : %s" +
                                        "\n Topic 4 : %s" +
                                        "\n Topic 5 : %s", user.getCompleteUntil(), getTopic1Progress(), getTopic2Progress(), getTopic3Progress(), getTopic4Progress(), getTopic5Progress()));


                progressBar = findViewById(R.id.progressBar);
                progressBar.setProgress(user.getCompleteUntil());

                btnContinue = findViewById(R.id.btnContinue);
                btnCert = findViewById(R.id.btnCert);

                if (completeUntil == 5) {
                    textViewCurrentProgress.setText(
                            String.format(
                                    "All Topics Completed!" +
                                            "\n Topic 1 : %s" +
                                            "\n Topic 2 : %s" +
                                            "\n Topic 3 : %s" +
                                            "\n Topic 4 : %s" +
                                            "\n Topic 5 : %s", getTopic1Progress(), getTopic2Progress(), getTopic3Progress(), getTopic4Progress(), getTopic5Progress()));

                    btnContinue.setText(R.string.backToRevision);
                    btnCert.setVisibility(View.VISIBLE);
                    btnCert.setOnClickListener(v -> {
                        Intent intent = new Intent(HomeActivity.this, CertActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userID", user.getuid());
                        intent.putExtra("name", user.getName());
                        startActivity(intent);
                    });

                } else {
                    if (completeUntil == 0) {
                        btnContinue.setText(R.string.GetStarted);
                    } else {
                        btnContinue.setText(R.string.continueOnProgress);
                    }
                }

                btnContinue.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, ReadingCornerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userID", user.getuid());
                    intent.putExtra("name", user.getName());
                    intent.putExtra("phoneNumber", user.getPhoneNumber());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("completeUntil", user.getCompleteUntil());
                    startActivity(intent);
                });

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userID", userID);
            intent.putExtra("name", name);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Function for share application
        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Learn about Dementia!");
            String shareMessage = "\nShare Dementia App with your family & friends! \nClick Link below to download\n\n";
            shareMessage = shareMessage + "https://drive.google.com/file/d/1szAdyMrCKM7haalciPiL0_dayNzypNjW/view?usp=sharing" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        });

        // Function for logout button
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

    private String getTopic1Progress() {
        if (completeUntil > 0) {
            return "Completed!";
        } else {
            return "Incomplete!";
        }
    }

    private String getTopic2Progress() {
        if (completeUntil > 1) {
            return "Completed!";
        } else {
            return "Incomplete!";
        }
    }

    private String getTopic3Progress() {
        if (completeUntil > 2) {
            return "Completed!";
        } else {
            return "Incomplete!";
        }
    }

    private String getTopic4Progress() {
        if (completeUntil > 3) {
            return "Completed!";
        } else {
            return "Incomplete!";
        }
    }

    private String getTopic5Progress() {
        if (completeUntil > 4) {
            return "Completed!";
        } else {
            return "Incomplete!";
        }
    }
}
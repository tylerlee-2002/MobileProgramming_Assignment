package com.example.mobileprogramming_assignment;

import static java.lang.Math.floor;
import static java.lang.Math.round;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class CourseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID, name, email, gender, dob;
    int completeUntil, mark;
    boolean isTopic1Done, isTopic2Done, isTopic3Done, isTopic4Done, isExamDone, isPassed = false;

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    navProfile();
                    return true;
                case R.id.navigationMyCourses:
                    navCourse();
                    return true;
                case R.id.navigationHome:
                    navHome();
                    return true;
                case R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        bottomNavigationView.setSelectedItemId(R.id.navigationMyCourses);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;

        db.collection("user").whereEqualTo("uid", mUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    userID = Objects.requireNonNull(document.getData().get("uid")).toString();
                    name = Objects.requireNonNull(document.getData().get("name")).toString();
                    gender = Objects.requireNonNull(document.getData().get("gender")).toString();
                    dob = Objects.requireNonNull(document.getData().get("dateOfBirth")).toString();
                    email = Objects.requireNonNull(document.getData().get("email")).toString();
                    isTopic1Done = Boolean.parseBoolean(Objects.requireNonNull(document.getData().get("topic1Done")).toString());
                    isTopic2Done = Boolean.parseBoolean(Objects.requireNonNull(document.getData().get("topic2Done")).toString());
                    isTopic3Done = Boolean.parseBoolean(Objects.requireNonNull(document.getData().get("topic3Done")).toString());
                    isTopic4Done = Boolean.parseBoolean(Objects.requireNonNull(document.getData().get("topic4Done")).toString());
                    isExamDone = Boolean.parseBoolean(Objects.requireNonNull(document.getData().get("examDone")).toString());
                    mark = Integer.parseInt(Objects.requireNonNull(document.getData().get("mark")).toString());
                }

                ProgressBar progressBar = findViewById(R.id.progressBar);
                ImageView topic1doneTick = findViewById(R.id.topic1doneTick);
                ImageView topic2doneTick = findViewById(R.id.topic2doneTick);
                ImageView topic3doneTick = findViewById(R.id.topic3doneTick);
                ImageView topic4doneTick = findViewById(R.id.topic4doneTick);
                ImageView examdoneTick = findViewById(R.id.examdoneTick);

                if (isTopic1Done) {
                    topic1doneTick.setVisibility(View.VISIBLE);
                    completeUntil += 1;
                }
                if (isTopic2Done) {
                    topic2doneTick.setVisibility(View.VISIBLE);
                    completeUntil += 1;
                }
                if (isTopic3Done) {
                    topic3doneTick.setVisibility(View.VISIBLE);
                    completeUntil += 1;
                }
                if (isTopic4Done) {
                    topic4doneTick.setVisibility(View.VISIBLE);
                    completeUntil += 1;
                }

                RelativeLayout examLayout = findViewById(R.id.examLayout);
                if (isTopic1Done && isTopic2Done && isTopic3Done && isTopic4Done){
                    examLayout.setVisibility(View.VISIBLE);
                }

                if (isExamDone && mark >= 60) {
                    completeUntil += 1;
                }

                if (mark >= 60) {
                    isPassed = true;
                }

                TextView textViewSub6Title = findViewById(R.id.textViewSub6Title);

                TextView resultDisplaytxt = findViewById(R.id.resultDisplaytxt);
                resultDisplaytxt.setText(String.format("Total Marks: %s%s", mark," / 100%"));
                TextView resitDescTxt = findViewById(R.id.resitDescTxt);

                RelativeLayout resitLayout = findViewById(R.id.resitLayout);
                if (isExamDone && mark < 60) {
                    examLayout.setVisibility(View.GONE);
                    resitLayout.setVisibility(View.VISIBLE);
                    textViewSub6Title.setText("Resit Exam!");
                } else if ((isExamDone && isPassed)) {
                    resitLayout.setVisibility(View.VISIBLE);
                    examLayout.setVisibility(View.GONE);
                    resitDescTxt.setText("View Your Exam Paper Now!");
                    textViewSub6Title.setText("View Result!");
                    examdoneTick.setVisibility(View.VISIBLE);
                }

                progressBar.setProgress(completeUntil);

                TextView progressTextView = findViewById(R.id.progressTextView);
                progressTextView.setText(String.format("%s%s", 20 * completeUntil, "%"));
                TextView nextTopicTextView = findViewById(R.id.nextTopicTextView);
                if (!isTopic1Done){
                    nextTopicTextView.setText("Next Topic: \nCognitive Changes!");
                } else if (!isTopic2Done) {
                    nextTopicTextView.setText("Next Topic: \nPsychological Changes!");
                } else if (!isTopic3Done) {
                    nextTopicTextView.setText("Next Topic: \nTips for Communicating!");
                } else if (!isTopic4Done) {
                    nextTopicTextView.setText("Next Topic: \nDealing with Trouble Person!");
                } else if (!isExamDone) {
                    nextTopicTextView.setText("Next Topic: \nFinal Exam!");
                } else if (!isPassed){
                    nextTopicTextView.setText(String.format("Next Topic: Resit Exam! \nLast Result: %s%s", mark,"/100%"));
                } else {
                    nextTopicTextView.setText("Next Topic: \nCheck your Certificate!");
                }

                CardView progressCard = findViewById(R.id.progressCard);
                progressCard.setOnClickListener(v -> {
                    if (!isTopic1Done){
                        Intent course1Intent = new Intent(this, ReadingCorner1.class);
                        course1Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(course1Intent);
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                    } else if (!isTopic2Done){
                        Intent course2Intent = new Intent(this, ReadingCorner2.class);
                        course2Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(course2Intent);
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                    } else if (!isTopic3Done){
                        Intent course3Intent = new Intent(this, ReadingCorner3.class);
                        course3Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(course3Intent);
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                    } else if (!isTopic4Done){
                        Intent course4Intent = new Intent(this, ReadingCorner4.class);
                        course4Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(course4Intent);
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                    } else if (!isExamDone){
                        Intent examIntent = new Intent(this, ExamActivity.class);
                        examIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(examIntent);
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                    } else if (!isPassed){
                        Intent resitIntent = new Intent(this, ResitActivity.class);
                        resitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(resitIntent);
                    } else {
                        // Redirect to profile page to view Certificate!
                        navProfile();
                    }
                });

                androidx.cardview.widget.CardView Course1Card = findViewById(R.id.Course1Card);
                Course1Card.setOnClickListener(v -> {
                    Intent course1Intent = new Intent(this, ReadingCorner1.class);
                    course1Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(course1Intent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                });

                androidx.cardview.widget.CardView Course2Card = findViewById(R.id.Course2Card);
                Course2Card.setOnClickListener(v -> {
                    Intent course2Intent = new Intent(this, ReadingCorner2.class);
                    course2Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(course2Intent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                });

                androidx.cardview.widget.CardView Course3Card = findViewById(R.id.Course3Card);
                Course3Card.setOnClickListener(v -> {
                    Intent course3Intent = new Intent(this, ReadingCorner3.class);
                    course3Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(course3Intent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                });

                androidx.cardview.widget.CardView Course4Card = findViewById(R.id.Course4Card);
                Course4Card.setOnClickListener(v -> {
                    Intent course4Intent = new Intent(this, ReadingCorner4.class);
                    course4Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(course4Intent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                });

                androidx.cardview.widget.CardView ExamCard = findViewById(R.id.ExamCard);
                ExamCard.setOnClickListener(v -> {
                    Intent examIntent = new Intent(this, ExamActivity.class);
                    examIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(examIntent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                });

                androidx.cardview.widget.CardView resitCard = findViewById(R.id.resitCard);
                resitCard.setOnClickListener(v -> {
                    Intent resitIntent = new Intent(this, ResitActivity.class);
                    resitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(resitIntent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigationHome) {
            navHome();
        } else if (id == R.id.navigationMyCourses) {
            navCourse();
        } else if (id == R.id.navigationMyProfile) {
            navProfile();
        } else if (id == R.id.nav_share) {
            navShare();
        } else if (id == R.id.btnLogout) {
            navLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void navHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
    public void navProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
    public void navCourse(){
//        Already at Course Page
//        Intent courseIntent = new Intent(this, CourseActivity.class);
//        courseIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(courseIntent);
//        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
    public void navShare(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Learn about Dementia!");
        String shareMessage = "\nShare Dementia App with your family & friends! \nClick Link below to download\n\n";
        shareMessage = shareMessage + "https://drive.google.com/file/d/10pE5zexfZFRUo_jOw3Bh_04LjiZuwo3A/view?usp=share_link" + BuildConfig.APPLICATION_ID + "\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "choose one"));
    }
    public void navLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform the action when the "Yes" button is clicked
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CourseActivity.this, SignInActivity.class);
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
    }
}
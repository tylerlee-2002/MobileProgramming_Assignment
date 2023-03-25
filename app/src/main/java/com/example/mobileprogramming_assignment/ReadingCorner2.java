package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReadingCorner2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    com.example.mobileprogramming_assignment.cairoButton id_done_Button;

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
        setContentView(R.layout.activity_reading_corner2);

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

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;

        id_done_Button = findViewById(R.id.id_done_Button);
        id_done_Button.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_quiz_window, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            TextView myTextView = popupView.findViewById(R.id.textView);
            myTextView.setText("What will become the case with psychological changes?");

            RadioGroup radioGroup = popupView.findViewById(R.id.radio_group);
            RadioButton RadioButton1 = popupView.findViewById(R.id.radio_button1);
            RadioButton RadioButton2 = popupView.findViewById(R.id.radio_button2);
            RadioButton RadioButton3 = popupView.findViewById(R.id.radio_button3);

            RadioButton1.setText("Appropriate behavior");
            RadioButton2.setText("Depression");
            RadioButton3.setText("Good Personality");

            Button closeButton = popupView.findViewById(R.id.closeButton);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.radio_button1:
                        closeButton.setText(R.string.wrongAnswer);
                        closeButton.setTextColor(0xeb3434);
                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
                        break;
                    case R.id.radio_button2:
                        closeButton.setText(R.string.correctAnswer);
                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.dark_green));
                        closeButton.setEnabled(true);
                        break;
                    case R.id.radio_button3:
                        closeButton.setText(R.string.tryAgain);
                        closeButton.setTextColor(getApplication().getResources().getColor(R.color.red));
                        closeButton.setEnabled(false);
                        break;
                }
            });

            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

            closeButton.setOnClickListener(task -> {
                Map<String, Object> updates = new HashMap<>();
                updates.put("topic2Done", true);
                db.collection("user").document(mUser.getUid()).update(updates).addOnSuccessListener(aVoid -> Log.d(TAG, "Topic Completed!")).addOnFailureListener(e -> Log.e(TAG, "Error updating user data", e));

                Intent intent = new Intent(this, CourseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                Toast.makeText(this, "Topic 2: Psychological Changes - Completed", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });
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
        Intent courseIntent = new Intent(this, CourseActivity.class);
        courseIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(courseIntent);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
    public void navShare(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Learn about Dementia!");
        String shareMessage = "\nShare Dementia App with your family & friends! \nClick Link below to download\n\n";
        shareMessage = shareMessage + "https://drive.google.com/file/d/1szAdyMrCKM7haalciPiL0_dayNzypNjW/view?usp=sharing" + BuildConfig.APPLICATION_ID + "\n\n";
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
                Intent intent = new Intent(ReadingCorner2.this, SignInActivity.class);
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
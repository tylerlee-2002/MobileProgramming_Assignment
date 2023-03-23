package com.example.mobileprogramming_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CourseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID, name, email, gender, dob;

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
//                    Intent intent = new Intent(CourseActivity.this, ProfileActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("userID", user.getuid());
//                    intent.putExtra("name", user.getName());
//                    intent.putExtra("email", user.getEmail());
//                    intent.putExtra("gender", user.getGender());
//                    intent.putExtra("dob", user.getDateOfBirth());
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
//                    return true;
                case R.id.navigationMyCourses:
                    return true;
                case R.id.navigationHome:
                    // Did Nothing, because already at home page
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigationHome) {
            // Did Nothing, because already at home page
        } else if (id == R.id.navigationMyCourses) {

        } else if (id == R.id.navigationMyProfile)
        {
//            Intent intent = new Intent(CourseActivity.this, ProfileActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("userID", user.getuid());
//            intent.putExtra("name", user.getName());
//            intent.putExtra("email", user.getEmail());
//            intent.putExtra("gender", user.getGender());
//            intent.putExtra("dob", user.getDateOfBirth());
//            startActivity(intent);
//            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
        } else if (id == R.id.nav_share)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Learn about Dementia!");
            String shareMessage = "\nShare Dementia App with your family & friends! \nClick Link below to download\n\n";
            shareMessage = shareMessage + "https://drive.google.com/file/d/1szAdyMrCKM7haalciPiL0_dayNzypNjW/view?usp=sharing" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));

        } else if (id == R.id.btnLogout)
        {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
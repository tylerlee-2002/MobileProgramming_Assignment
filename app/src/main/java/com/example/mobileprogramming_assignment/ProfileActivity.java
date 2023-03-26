package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID, name, email, gender, dob;
    String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    int mark;
    boolean isPassed = false;
    Bitmap bmp, scaledBmp;
    Button btnDownload;
    RelativeLayout certLayout;
    LinearLayout contentLayout;
    TextView txtName, txtEmail, txtDob, certTextView, txtCertName, changePw;
    ImageView profilePic;
    boolean isTopic1Done, isTopic2Done, isTopic3Done, isTopic4Done, isExamDone;

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
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
                case  R.id.navigationMenu:
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
        setContentView(R.layout.activity_profile);
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

        bottomNavigationView.setSelectedItemId(R.id.navigationMyProfile);

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

                if (mark >= 60) {
                    isPassed = true;
                }

                txtName = findViewById(R.id.txtName);
                txtName.setText(name);

                txtEmail = findViewById(R.id.txtEmail);
                txtEmail.setText(email);

                txtDob = findViewById(R.id.txtDob);
                txtDob.setText(dob);

                changePw = findViewById(R.id.changePw);
                changePw.setOnClickListener(view -> {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Information");
                            builder.setMessage("Email sent! \n\n Please check your email to reset.");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Perform the action when the "Yes" button is clicked
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(this, "System Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                contentLayout = findViewById(R.id.contentLayout);

                profilePic = findViewById(R.id.profilePic);
                if (Objects.equals(gender, "Male")) {
                    profilePic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.male_pic, null));
                } else {
                    profilePic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.female_pic, null));
                }

                if (isTopic1Done && isTopic2Done && isTopic3Done && isTopic4Done && isExamDone && isPassed) {
                    contentLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.top_background_1, null));

                    certTextView = findViewById(R.id.certTextView);
                    certTextView.setVisibility(View.VISIBLE);

                    txtCertName = findViewById(R.id.txtUsername);
                    txtCertName.setText(name);

                    certLayout = findViewById(R.id.certLayout);
                    certLayout.setVisibility(View.VISIBLE);

                    btnDownload = findViewById(R.id.btnDownload);
                    btnDownload.setVisibility(View.VISIBLE);
                    btnDownload.setOnClickListener(v -> {
                        generatePDF();
                    });
                }
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
        // Handle navigation view item clicks here.
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

    private void generatePDF() {

        int pageHeight = 1120;
        int pageWidth = 792;

        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.certificate);
        scaledBmp = Bitmap.createScaledBitmap(bmp, pageWidth, pageHeight, false);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scaledBmp, 0, 0, paint);

        // below line is used for setting our text to center of PDF.
        title.setTypeface(ResourcesCompat.getFont(this, R.font.alex_brush));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextAlign(Paint.Align.CENTER);
        title.setTextSize(65);

        // Draw content on the canvas using standard Android drawing methods
        canvas.drawText(name, 396, 600, title);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Cert.pdf");
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            document.finishPage(page);
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Certificate generated successfully.", Toast.LENGTH_SHORT).show();
            document.close();

            sendNotification();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void sendNotification() {

        NotificationChannel channel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("Cert_generated", "Certificate", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Cert generated successfully!");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        String stringFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/Cert.pdf";
        File file = new File(stringFile);

        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
        }

        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Cert_generated")
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Certificate download completed!")
                .setContentText("Saved at Files -> Download -> Cert.pdf!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    public void navHome(){
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
    public void navProfile(){
//        Already at Profile.
//        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
    public void navCourse(){
        Intent courseIntent = new Intent(ProfileActivity.this, CourseActivity.class);
        courseIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(courseIntent);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
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
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
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
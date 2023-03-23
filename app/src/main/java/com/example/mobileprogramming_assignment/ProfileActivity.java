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
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    // Did Nothing, because already at profile page
                    return true;
                case R.id.navigationMyCourses:
                    return true;
                case R.id.navigationHome:
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                    return true;
                case  R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    String userID, name, email, gender, dob;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // creating a bitmap variable for storing our images
    Bitmap bmp, scaledBmp;
    Button btnDownload;
    RelativeLayout certLayout;
    TextView txtName, txtEmail, txtDob, txtGender, certTextView, txtCertName;
    ImageView genderImageView;

//    TextView txtName, txtEmail, txtPhoneNumber;
//    EditText inputPhoneNumber;
//    Button btnConfirm;
//    ImageButton btnClose;

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

        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        gender = getIntent().getStringExtra("gender");
        dob = getIntent().getStringExtra("dob");

        txtName = findViewById(R.id.txtName);
        txtName.setText(name);

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(email);

        txtDob = findViewById(R.id.txtDob);
        txtDob.setText(dob);

        txtGender = findViewById(R.id.txtGender);
        txtGender.setText(gender);

        genderImageView = findViewById(R.id.genderImageView);
        if(Objects.equals(gender, "male")){
            genderImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.male, null));
        } else {
            genderImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.female, null));
        }

        // Check whether user complete all courses & quizzes
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

//        txtName = findViewById(R.id.txtName);
//        txtName.setText(String.format("Name: %s", name));
//
//        txtEmail = findViewById(R.id.txtEmail);
//        txtEmail.setText(String.format("Email: %s", email));
//
//        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
//        txtPhoneNumber.setText(String.format("Tel: %s", phoneNumber));
//
//        btnClose = findViewById(R.id.btnClose);
//        btnClose.setOnClickListener(v -> {
//            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
//        });
//
//        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
//        btnConfirm = findViewById(R.id.btnConfirm);
//        btnConfirm.setOnClickListener(v -> {
//            String phoneNumber = inputPhoneNumber.getText().toString();
//
//            if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
//                inputPhoneNumber.setError("Enter Correct Phone Number");
//            } else {
//                Map<String, Object> updates2 = new HashMap<>();
//                updates2.put("phoneNumber", phoneNumber);
//                FirebaseFirestore.getInstance().collection("user").document(userID).update(updates2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        Toast.makeText(ProfileActivity.this, "New Phone Number Updated!", Toast.LENGTH_SHORT).show();
//                        startActivity(intent);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ProfileActivity.this, "Error updating document!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
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
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
            return true;
        } else if (id == R.id.navigationMyCourses) {

        } else if (id == R.id.navigationMyProfile) {
            // Did Nothing, because already at profile page
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
                    Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
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

}
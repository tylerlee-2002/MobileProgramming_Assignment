package com.example.mobileprogramming_assignment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION.SDK_INT;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView createNewAccount;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView btnGoogle;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking user permissions.
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (checkPermission()) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                requestPermission();
            }
        }

        createNewAccount = findViewById(R.id.createNewAccount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        createNewAccount.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> performLogin());

        btnGoogle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
        });

//      Notification Function
        NotificationChannel();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 12);
        calendar.set(Calendar.SECOND, 10);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(MainActivity.this, NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        if (SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void NotificationChannel() {

        if (SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Dementia";
            String description = "Dementia Application!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void performLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Correct Email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Correct Password");
        } else {
            progressDialog.setMessage("Please Wait while Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    FirebaseFirestore.getInstance().collection("user").whereEqualTo("uid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            if (task2.getResult().isEmpty()) {
                                Intent intent = new Intent(MainActivity.this, VerifyActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "First Time Login!", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Invalid Email or Wrong Password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();

        } else {
            int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}
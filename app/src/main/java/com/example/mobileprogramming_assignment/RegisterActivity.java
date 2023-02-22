package com.example.mobileprogramming_assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    TextView alreadyHaveAccount;
    EditText inputEmail, inputPassword, inputConfirmPassword;
    Button btnRegister;
    String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        btnRegister = findViewById(R.id.btnRegister);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        alreadyHaveAccount.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, MainActivity.class)));
        btnRegister.setOnClickListener(v -> PerformAuth());
    }

    private void PerformAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Correct Email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Correct Password");
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPassword.setError("Password does not Match!");
        } else {
            progressDialog.setMessage("Please Wait while Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            progressDialog.dismiss();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "User already registered!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
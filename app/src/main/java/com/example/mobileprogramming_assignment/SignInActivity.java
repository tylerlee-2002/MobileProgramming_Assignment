package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    cairoButton _signInButton;
    Button _signInGoogle_Button;
    cairoEditText _emailEditText, _passwordEditText;
    cairoTextView _forgetPasswordTextView ,_signUpTextView;
    String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        /////*     initialize view   */////
        _signInButton = findViewById(R.id.id_signIn_Button);
        _emailEditText = findViewById(R.id.id_email_EditText);
        _passwordEditText = findViewById(R.id.id_password_EditText);
        _forgetPasswordTextView = findViewById(R.id.id_forgetPassword_TextView);
        _signUpTextView = findViewById(R.id.id_SignUP_TextView);
        _signInGoogle_Button = findViewById(R.id.id_signInGoogle_Button);

        /////*     On Click         */////
        _signInButton.setOnClickListener(this);
        _signInGoogle_Button.setOnClickListener(this);
        _forgetPasswordTextView.setOnClickListener(this);
        _signUpTextView.setOnClickListener(this);

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }

    @Override
    public void onClick(View v) {
        if
        (v == _forgetPasswordTextView) {
            resetPassword();
        }

        if
        (v == _signInButton) {
            signIn();
        }

        if
        (v == _signUpTextView) {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        }

        if
        (v == _signInGoogle_Button) {
            Intent intent = new Intent(this, GoogleSignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        }
    }

    private void signIn() {
        String email = Objects.requireNonNull(_emailEditText.getText()).toString();
        String password = Objects.requireNonNull(_passwordEditText.getText()).toString();

        if (!email.matches(emailPattern)) {
            _emailEditText.setError("Enter Correct Email");
        } else if (password.isEmpty() || password.length() < 6) {
            _passwordEditText.setError("Enter Correct Password");
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
                                Intent intent = new Intent(SignInActivity.this, NewUserActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                                Toast.makeText(SignInActivity.this, "New User", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                                Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Invalid Email or Wrong Password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void resetPassword() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_reset_password, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        EditText inputEmail = popupView.findViewById(R.id.inputEmail);
        Button resetButton = popupView.findViewById(R.id.resetButton);
        ImageButton btnClose = popupView.findViewById(R.id.btnClose);

        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        btnClose.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        resetButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();

            if (!email.matches(emailPattern)) {
                inputEmail.setError("Enter Correct Email");
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Request Sent! Please check email!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Email sent.");
                        popupWindow.dismiss();
                    } else {
                        Toast.makeText(this, "Email not registered yet!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

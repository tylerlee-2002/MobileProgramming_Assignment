package com.example.mobileprogramming_assignment;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileprogramming_assignment.cairoButton;
import com.example.mobileprogramming_assignment.cairoEditText;
import com.example.mobileprogramming_assignment.cairoTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{

    String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
    cairoEditText _nameEditText,_emailEditText,_passwordEditText;
    cairoButton _signUpButton;
    Button _signInGoogle_Button;
    cairoTextView _login;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        /////*     initialize view   */////
        _login = findViewById(R.id.id_login_TextView);
        _nameEditText = findViewById(R.id.id_fullName_EditText);
        _emailEditText = findViewById(R.id.id_email_EditText);
        _passwordEditText = findViewById(R.id.id_password_EditText);
        _signUpButton = findViewById(R.id.id_signUp_Button);
        _signInGoogle_Button = findViewById(R.id.id_signInGoogle_Button);

        /////*     On Click         */////
        _login.setOnClickListener(this);
        _signUpButton.setOnClickListener(this);
        _signInGoogle_Button.setOnClickListener(this);

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
    public void onClick(View v)
    {
        if (v == _login)
        {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
        }
        if (v==_signUpButton)
        {
            signupfunction();
        }

        if
        (v == _signInGoogle_Button) {
            Intent intent = new Intent(this, GoogleSignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        }
    }

    private void signupfunction()
    {
        /////*   Get  Email  && Name  && Password    */////
        String fullName = Objects.requireNonNull(_nameEditText.getText()).toString();
        String email = Objects.requireNonNull(_emailEditText.getText()).toString();
        String password = Objects.requireNonNull(_passwordEditText.getText()).toString();

        /////*   Check if email and password written and valid   */////
        if (!validate())
        {
            return;
        }else
        {
            signup(fullName,email,password);

        }
    }


    public boolean validate()
    {
        boolean valid = true;

        String fullName = Objects.requireNonNull(_nameEditText.getText()).toString();
        String email = Objects.requireNonNull(_emailEditText.getText()).toString();
        String password = Objects.requireNonNull(_passwordEditText.getText()).toString();

        if (fullName.isEmpty())
        {
            _nameEditText.setError(getString(R.string.enteryourname));
            valid = false;
        } else
        {
            _nameEditText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailEditText.setError(getString(R.string.validemail));
            valid = false;
        } else
        {
            _emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12)
        {
            _passwordEditText.setError(getString(R.string.validpassword));
            valid = false;
        } else
        {
            _passwordEditText.setError(null);
        }

        return valid;
    }

    private void signup(String fullName, String email, String password)
    {
        progressDialog.setMessage("Please Wait while Registration...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.dismiss();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "User already registered!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

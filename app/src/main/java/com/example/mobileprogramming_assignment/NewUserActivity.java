package com.example.mobileprogramming_assignment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import java.util.Arrays;
import java.util.Objects;

import com.example.mobileprogramming_assignment.cairoButton;
import com.example.mobileprogramming_assignment.cairoEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class NewUserActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final int PICK_IMAGE = 1;
    private Uri selectedImageUri;
    String userID, email;
    DatePicker _dayDatePicker ,_monthDatePicker , _yearDatePicker;
    cairoEditText _userNameEditText;
    cairoButton _doneButton;
    Button _uploadPictureButton;
    customSwitch _genderCustomSwitch;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        userID = mUser.getUid();
        email = mUser.getEmail();

        /////*     initialize view   */////
        _uploadPictureButton = findViewById(R.id.id_uploadPicture_Button);
        _userNameEditText = findViewById(R.id.id_userName_EditText);
        _dayDatePicker  = (DatePicker) findViewById(R.id.id_day_DatePicker);
        _monthDatePicker = (DatePicker) findViewById(R.id.id_month_DatePicker);
        _yearDatePicker  = (DatePicker) findViewById(R.id.id_year_DatePicker);
        _genderCustomSwitch = findViewById(R.id.id_gender_customSwitch);
        _doneButton = findViewById(R.id.id_done_Button);

        /////*    add days , month and year         */////
        _dayDatePicker.setOffset(1);
        _dayDatePicker.setSeletion(5);
        _dayDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.days)));

        _monthDatePicker.setOffset(1);
        _monthDatePicker.setSeletion(1);
        _monthDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.months)));

        _yearDatePicker.setOffset(1);
        _yearDatePicker.setSeletion(95);
        _yearDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.years)));

        /////*     On Click         */////
        _doneButton.setOnClickListener(this);
        _uploadPictureButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v)
    {
            if (v == _doneButton)
            {
                complete();
            }
            if (v==_uploadPictureButton)
            {
                pickImage();
            }
    }

    private void pickImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void complete()
    {
        String _gender = "";
        String _username = Objects.requireNonNull(_userNameEditText.getText()).toString();
        String getGender = _genderCustomSwitch.getChecked().toString();
        String _day = _dayDatePicker.getSeletedItem();
        String _month = _monthDatePicker.getSeletedItem();
        String _year = _yearDatePicker.getSeletedItem();
        String _dob = _day + " " + _month + " " + _year ;
        if (getGender.equals("LEFT")){
            _gender = "female";
        }else {
            _gender = "male";
        }

        /////*   Check if username ,gender and date of birth  are entered     */////
        if (!validate(_username, _gender, _dob)) {
            return;
        } else {
            addData(_username, _gender, _dob);
        }
    }

    private void addData(String username, String gender, String dob)
    {
        progressDialog.setMessage("Updating Profile...");
        progressDialog.setTitle("User Personal Info");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String imagePath = selectedImageUri.getPath();



        Toast.makeText(this, "success" + username + " " + gender + " " + dob, Toast.LENGTH_LONG).show();
    }


    public boolean validate(String username, String gender, String dateofbirth)
    {
        boolean valid = true;

        if (username.isEmpty())
        {
            _userNameEditText.setError(getString(R.string.validusername));
            valid = false;
        }
        if (gender.isEmpty())
        {
            Toast.makeText(this, "please select your gender", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (dateofbirth.equals(""))
        {
            Toast.makeText(this, "please select your gender", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            selectedImageUri = data.getData();
            // String imagePath = selectedImageUri.getPath();
        }
    }
}

package com.example.mobileprogramming_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Arrays;
import java.util.Objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class NewUserActivity extends AppCompatActivity implements View.OnClickListener
{
    String userID, email;
    DatePicker _dayDatePicker ,_monthDatePicker , _yearDatePicker;
    cairoEditText _userNameEditText;
    cairoButton _doneButton;
    customSwitch _genderCustomSwitch;
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
    }

    @Override
    public void onClick(View v)
    {
            if (v == _doneButton)
            {
                complete();
            }
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
            _gender = "Female";
        }else {
            _gender = "Male";
        }

        /////*   Check if username ,gender and date of birth  are entered     */////
        if (!validate(_username, _gender, _dob)) {
            return;
        } else {
            addData(_username, _gender, _dob);
        }
    }

    private void addData(String name, String gender, String dob)
    {
        UserInfo newUser = new UserInfo(userID, name, email, gender, dob, false, false, false, false, false, 0);
        db.collection("user").document(userID).set(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                Toast.makeText(this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Firebase error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validate(String username, String gender, String dob)
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
        if (dob.equals(""))
        {
            Toast.makeText(this, "please select your gender", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }
}

package com.example.mobileprogramming_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ResitActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButtonOption1;
    private RadioButton radioButtonOption2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resit);

        radioGroup = findViewById(R.id.radio_group);
        radioButtonOption1 = findViewById(R.id.radio_button_option1);
        radioButtonOption2 = findViewById(R.id.radio_button_option2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_option1) {
                    radioButtonOption1.setBackgroundColor(ContextCompat.getColor(ResitActivity.this, R.color.green));
                    radioButtonOption2.setBackgroundColor(ContextCompat.getColor(ResitActivity.this, R.color.red));
                } else if (checkedId == R.id.radio_button_option2) {
                    radioButtonOption1.setBackgroundColor(ContextCompat.getColor(ResitActivity.this, R.color.red));
                    radioButtonOption2.setBackgroundColor(ContextCompat.getColor(ResitActivity.this, R.color.green));
                }
            }
        });
    }
}
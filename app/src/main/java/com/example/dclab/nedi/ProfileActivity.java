package com.example.dclab.nedi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    public static final String SET_NAME = "홍길동";
    public static final String SET_SEX = "남자";
    public static final String SET_AGE = "12";

    private EditText set_name;
    private String set_sex = "남자";
    private RadioGroup BoyOrGirl;
    private RadioButton male;
    private RadioButton female;
    private NumberPicker set_age;
    private String[] Age = {"5세", "6세", "7세", "8세", "9세", "10세", "11세", "12세", "13세", "14세", "15세", "16세", "17세", "18세", "19세", "20세"};

    private Button button_set_profile;

    private String[] profileBuf = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("개인 정보 입력");

        Log.e("Profile_activity", "Success");

        final Intent intent = getIntent();
        profileBuf[0] = intent.getStringExtra(SET_NAME);
        profileBuf[1] = intent.getStringExtra(SET_SEX);
        profileBuf[2] = intent.getStringExtra(SET_AGE);

        BoyOrGirl = findViewById(R.id.radioGroup);
        male = findViewById(R.id.set_male);
        female = findViewById(R.id.set_female);
        button_set_profile = findViewById(R.id.Button_profile_set);
        button_set_profile.setOnClickListener(profileOnClickListener);

        BoyOrGirl.check(R.id.set_male);
        set_name = findViewById(R.id.set_name);
        set_age = findViewById(R.id.set_age);
        set_age.setMinValue(0);
        set_age.setMaxValue(Age.length - 1);
        set_age.setDisplayedValues(Age);
        set_age.setValue(1);//6세
        set_age.setWrapSelectorWheel(true);
    }

    Button.OnClickListener profileOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.Button_profile_set) {
                if (set_name.getText().toString().matches("")){
                    Toast.makeText(ProfileActivity.this,"이름을 입력해주세요.",Toast.LENGTH_LONG).show();
                }else{
                    if (male.isChecked()) {
                        set_sex = "남자";
                    } else if (female.isChecked()) {
                        set_sex = "여자";
                    }
                    final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(MainActivity.NAME, String.valueOf(set_name.getText()));
                    intent.putExtra(MainActivity.SEX, String.valueOf(set_sex));
                    intent.putExtra(MainActivity.AGE, String.valueOf(Age[set_age.getValue()]));
                    startActivity(intent);
                    finish();
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }

}

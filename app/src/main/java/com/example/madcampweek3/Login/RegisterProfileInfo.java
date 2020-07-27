package com.example.madcampweek3.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madcampweek3.R;
import com.example.madcampweek3.Utils.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Grocery App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class RegisterProfileInfo extends AppCompatActivity {

    String password; //Intent로 전달 받음
    User user; //Intent로 전달 받음
    boolean male = true;

    private Button ContinueButton;
    private Button maleSelectionButton;
    private Button femaleSelectionButton;
    private String username, school, major, job;
    private EditText mUsername, mSchool, mMajor, mJob;

    //나이 설정
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    private DatePicker ageSelectionPicker;
    private int ageLimit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile_info);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");

        maleSelectionButton = findViewById(R.id.maleSelectionButton);
        femaleSelectionButton = findViewById(R.id.femaleSelectionButton);
        ContinueButton = findViewById(R.id.btn_register_profile_next);
        mUsername = findViewById(R.id.register_username);
        mSchool = findViewById(R.id.register_school);
        mMajor = findViewById(R.id.register_major);
        mJob = findViewById(R.id.register_job);
        ageSelectionPicker = findViewById(R.id.ageSelectionPicker);
        //By default male has to be selected so below code is added




        maleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButtonSelected();
            }
        });

        femaleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButtonSelected();
            }
        });

        ContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceEntryPage();
            }
        });

    }

    public void maleButtonSelected() {
        male = true;
        maleSelectionButton.setBackgroundColor(getColor(R.color.mainColor));
        maleSelectionButton.setAlpha(1.0f);
        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(getColor(R.color.white));
    }

    public void femaleButtonSelected() {
        male = false;
        femaleSelectionButton.setBackgroundColor(getColor(R.color.mainColor));
        femaleSelectionButton.setAlpha(1.0f);
        maleSelectionButton.setAlpha(.5f);
        maleSelectionButton.setBackgroundColor(getColor(R.color.white));
    }

    public void openPreferenceEntryPage() {

        //set gender
        String ownSex = male ? "male" : "female";
        user.setSex(ownSex);

        //set default photo
        String defaultPhoto = male ? "defaultMale" : "defaultFemale";
        user.setProfileImageUrl(defaultPhoto);

        //set profile info
        username = mUsername.getText().toString();
        school=mSchool.getText().toString();
        major=mMajor.getText().toString();
        job= mJob.getText().toString();

        user.setUsername(username);
        user.setSchool(school);
        user.setMajor(major);
        user.setJob(job);


        //set age

        int age = getAge(ageSelectionPicker.getYear(), ageSelectionPicker.getMonth(), ageSelectionPicker.getDayOfMonth());

        if(checkInputs(username,school,major,job)) {
        // if user is above 13 years old then only he/she will be allowed to register to the system.
              if (age > ageLimit) {
            // code for converting date to string
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, ageSelectionPicker.getYear());
                    cal.set(Calendar.MONTH, ageSelectionPicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, ageSelectionPicker.getDayOfMonth());
                    Date dateOfBirth = cal.getTime();
                    String strDateOfBirth = dateFormatter.format(dateOfBirth);

                    // code to set the dateOfBirthAttribute.
                    user.setDateOfBirth(age);


                    Intent intent = new Intent(this, RegisterAdditional.class);
                    intent.putExtra("password", password);
                    intent.putExtra("classUser", user);
                    startActivity(intent);
                    finish();

              } else {
                  Toast.makeText(getApplicationContext(), "20살 이상의 성인만 가입할 수 있습니다. ", Toast.LENGTH_SHORT).show();
              }
        }

    }




    private int getAge(int year, int month, int day) {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dateOfBirth.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private boolean checkInputs(String username, String school, String major ,String job) {
        if (username.equals("") || school.equals("")|| major.equals("")|| job.equals("")) {
            Toast.makeText(RegisterProfileInfo.this, "모든 정보를 기입해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

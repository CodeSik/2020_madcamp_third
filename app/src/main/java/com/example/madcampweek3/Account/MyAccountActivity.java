package com.example.madcampweek3.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.madcampweek3.MainActivity.MainActivity;
import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyAccountActivity extends AppCompatActivity {


    ViewPager viewPager;
    ViewPagerAdapter adapter;
    private TextView mheight, mjob, mhobby, msmoke, mdrink, mself_instruction, mschool, mmajor,musername,mage;
    private int age;
    private String region, userName;
    private String friendID, userID;



    private SharedPreferences appData;



    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
        startActivity(intent);
        MyAccountActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get friend ID & user ID */
        Intent intent = getIntent();
        //friendID = intent.getStringExtra("friendID");
        appData =getSharedPreferences("appData", MODE_PRIVATE);
        userID = appData.getString("ID","");

        setContentView(R.layout.activity_my_account);


        viewPager = (ViewPager) findViewById(R.id.myprofile_image);
        adapter= new ViewPagerAdapter (this, userID);
        viewPager.setAdapter(adapter);


        mself_instruction = findViewById(R.id.myprofile_selfInstruction);
        mheight = findViewById(R.id.myprofile_height);
        mschool = findViewById(R.id.myprofile_school);
        mmajor = findViewById(R.id.myprofile_major);
        mjob = findViewById(R.id.myprofile_job);
        mhobby = findViewById(R.id.myprofile_hobby);
        msmoke = findViewById(R.id.myprofile_smoke);
        mdrink = findViewById(R.id.myprofile_drink);
        musername = findViewById(R.id.myprofile_username);
        mage=findViewById(R.id.myprofile_age);
        initUi();
    }

    private void initUi() {

        setProfileInfo(userID);
    }



    private void setProfileInfo(String userID) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        AccountService service = retrofit.create(AccountService.class);

        /* Send image download request */
        service.downloadProfile(userID).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Profile download failure
                        assert response.errorBody() != null;
                        Log.d("LoginService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Profile download success
                    Log.d("LoginService", "res:" + response.message());

                    /* Change profile info */
                    if (response.body().has("userName")) {
                        String userName_str = response.body().get("userName").toString();
                        musername.setText(userName_str.substring(1, userName_str.length() - 1));
                    }
                    if (response.body().has("age")) {
                        mage.setText(new Integer(response.body().get("age").getAsInt()).toString() +"살");;
                    }
                    if (response.body().has("region")) {
                        String region_str = response.body().get("region").toString();
                        region=(region_str.substring(1, region_str.length() - 1));
                    }
                    if (response.body().has("height")) {

                        mheight.setText(response.body().get("height").toString());
                    }
                    if (response.body().has("job")) {
                        String job_str = response.body().get("job").toString();
                        mjob.setText(job_str.substring(1, job_str.length() - 1));
                    }
                    if (response.body().has("hobby")) {
                        String hobby_str = response.body().get("hobby").toString();
                        mhobby.setText(hobby_str.substring(1, hobby_str.length() - 1));
                    }
                    if (response.body().has("smoke")) {
                        Boolean smoke_bol = Boolean.parseBoolean(response.body().get("smoke").toString());
                        if (smoke_bol) {
                            msmoke.setText("흡연");
                        }
                        else {
                            msmoke.setText("비흡연");
                        }
                    }
                    if (response.body().has("drink")) {
                        Boolean drink_bol = Boolean.parseBoolean(response.body().get("drink").toString());
                        if (drink_bol) {
                            mdrink.setText("자주");
                        }
                        else {
                            mdrink.setText("안마심");
                        }
                    }
                    if (response.body().has("self_instruction")) {
                        String self_instruction_str = response.body().get("self_instruction").toString();
                        mself_instruction.setText(self_instruction_str.substring(1, self_instruction_str.length() - 1));
                    }
                    if (response.body().has("school")) {
                        String school_str = response.body().get("school").toString();
                        mschool.setText(school_str.substring(1, school_str.length() - 1));
                    }

                    if (response.body().has("major")) {
                        String major_str = response.body().get("major").toString();
                        mmajor.setText(major_str.substring(1, major_str.length() - 1));
                    }

                }
            }
            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d("ProfileService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }
}

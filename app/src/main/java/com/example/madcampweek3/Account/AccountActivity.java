package com.example.madcampweek3.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.madcampweek3.MainActivity.MainActivity;
import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.ImageService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.madcampweek3.Account.AccountEditFragment.PROFILE_IMAGE_KIND;
import static com.example.madcampweek3.Account.AccountEditFragment.PROFILE_IMAGE_NAME;

public class AccountActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    ViewPager viewPager;
    ViewPagerAdapter adapter;
    private TextView mheight, mjob, mhobby, msmoke, mdrink, mself_instruction, mschool, mmajor;
    private int age;
    private String region, friendName;
    private String friendID, userID;


    @BindView(R.id.toolbar_header_view)
    protected HeaderView toolbarHeaderView;

    @BindView(R.id.float_header_view)
    protected HeaderView floatHeaderView;

    @BindView(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private boolean isHideToolbarView = false;
    private SharedPreferences appData;


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get friend ID & user ID */
        Intent intent = getIntent();
        friendID = intent.getStringExtra("friendID");
        appData =getSharedPreferences("appData", MODE_PRIVATE);
        userID = appData.getString("ID","");

        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.profile_image);
        adapter= new ViewPagerAdapter (this, friendID);
        viewPager.setAdapter(adapter);


        mself_instruction = findViewById(R.id.profile_selfInstruction);
        mheight = findViewById(R.id.profile_height);
        mschool = findViewById(R.id.profile_school);
        mmajor = findViewById(R.id.profile_major);
        mjob = findViewById(R.id.profile_job);
        mhobby = findViewById(R.id.profile_hobby);
        msmoke = findViewById(R.id.profile_smoke);
        mdrink = findViewById(R.id.profile_drink);

        initUi();
    }

    private void initUi() {
        appBarLayout.addOnOffsetChangedListener(this);


        setProfileInfo(friendID);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
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
                        friendName=response.body().get("userName").toString();
                    }
                    if (response.body().has("age")) {
                        age=response.body().get("age").getAsInt();
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
                    toolbarHeaderView.bindTo(friendName, age, region);
                    floatHeaderView.bindTo(friendName, age, region);
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
package com.example.madcampweek3.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.madcampweek3.MainActivity.MainActivity;
import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.FriendService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    ViewPager viewPager;
    ViewPagerAdapter adapter;
    private TextView mheight, mjob, mhobby, msmoke, mdrink, mself_instruction, mschool, mmajor;
    private int age;
    private String region, friendName;
    private String friendID, userID;
    private RatingBar ratingbar;
    private TextView likeButton;
    ArrayList<String> result = new ArrayList<>();
    JsonArray LikeIDList = new JsonArray ();
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
    private boolean likeSuccess;


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    //TODO: FriendID의 받은 좋아요 리스트를 불러온 다음에 userID가 존재한다면 '보냄'으로 표시하도록 하기.

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
        likeButton = findViewById(R.id.like_button);

        /*Get FriendID 's Like List -> To set LikeButton correctly*/
        getLikeStatus();


        /* Set rating bar */
        ratingbar = findViewById(R.id.rating);
        ratingbar.setOnRatingBarChangeListener(new RatingListener());


        /*Set Like Button*/
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLike();
            }
        });
        initUi();
    }

    private void getLikeStatus() {
        Retrofit retrofit = RetrofitClient.getInstnce();
        AccountService service = retrofit.create(AccountService.class);


        service.getLike(friendID).enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body()==null)
                {
                    try { // Failure
                        assert response.errorBody() != null;
                        Log.d("FriendService", "res: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {//Success
                    Log.d("AccountService", "res: " + response.body().toString());
                    if (response.body().has("friendID")) {
                        LikeIDList = response.body().getAsJsonArray("friendID");
                    }
                    for (int i = 0; i < LikeIDList.size(); ++i) {
                        String friendID = LikeIDList.get(i).toString();
                        friendID = friendID.substring(1, friendID.length() - 1);
                        result.add(friendID);
                    }
                    if(result.contains(userID)){
                        likeButton.setText("보냄");
                        likeButton.setBackground(getDrawable(R.drawable.like_click));
                        likeButton.setEnabled(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void sendLike(){
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        FriendService service = retrofit.create(FriendService.class);

        /* Get proflie info*/
        JsonObject like_body = new JsonObject();
        like_body.addProperty("id",userID);
        like_body.addProperty("friendID",friendID);

        service.sendLike(like_body).enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Failure
                        assert response.errorBody() != null;
                        Log.d("FriendService", "res: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // Success
                    Log.d("FriendService", "res: " + response.body().toString());

                    likeSuccess = response.body().get("message").getAsBoolean();

                    likeButton.setText("보냄");
                    likeButton.setBackground(getDrawable(R.drawable.like_click));
                    likeButton.setEnabled(false);


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("FriendService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }

        });

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
                        String friend_str =response.body().get("userName").toString();
                        friendName = friend_str.substring(1,friend_str.length()-1);
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

    class RatingListener implements  RatingBar.OnRatingBarChangeListener {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingBar.setIsIndicator(true);
            ratingBar.setRating(rating);
            /* Init retrofit */
            Retrofit retrofit = RetrofitClient.getInstnce();
            FriendService service = retrofit.create(FriendService.class);

            /* Send star */
            JsonObject body = new JsonObject();
            body.addProperty("id", friendID);
            body.addProperty("score", rating);

            service.sendStar(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() == null) {
                        try { // Failure
                            assert response.errorBody() != null;
                            Log.d("LoginService", "res:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else { // Success
                        Toast.makeText(getApplication(), "별점을 보냈습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("FriendService", "Failed API call with call: " + call
                            + ", exception: " + t);
                }
            });

        }
    }
}
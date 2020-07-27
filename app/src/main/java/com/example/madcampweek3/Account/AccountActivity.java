package com.example.madcampweek3.Account;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import static com.example.madcampweek3.Account.AccountEditFragment.userID;

public class AccountActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static String userName = "seo"; // TODO: Change it into actual userName
    private ImageView profileImage;
    private TextView age, region, height, job, hobby, smoke, drink, self_instruction;

    @BindView(R.id.toolbar_header_view)
    protected HeaderView toolbarHeaderView;

    @BindView(R.id.float_header_view)
    protected HeaderView floatHeaderView;

    @BindView(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private boolean isHideToolbarView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        age = (TextView) findViewById(R.id.age);
        region = (TextView) findViewById(R.id.region);
        height = (TextView) findViewById(R.id.height);
        job = (TextView) findViewById(R.id.job);
        hobby = (TextView) findViewById(R.id.hobby);
        smoke = (TextView) findViewById(R.id.smoke);
        drink = (TextView) findViewById(R.id.drink);
        self_instruction = (TextView) findViewById(R.id.self_instruction);


        // TODO: Suport multiple images
        profileImage = (ImageView) findViewById(R.id.profile_image);

        initUi();
    }

    private void initUi() {
        appBarLayout.addOnOffsetChangedListener(this);


        setProfileImage(userID, profileImage, 1);
        setProfileInfo(userID);

        //TODO: 서버의 데이터로 바꿔야함
        toolbarHeaderView.bindTo(userName, "Last seen today at 7.00PM");
        floatHeaderView.bindTo(userName, "Last seen today at 7.00PM");
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

    private void setProfileImage(String userID, ImageView imageView, int position) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ImageService service = retrofit.create(ImageService.class);

        /* Send image download request */
        String fileName = position + "_" + PROFILE_IMAGE_NAME;
        service.downloadProfile(userID, PROFILE_IMAGE_KIND, fileName).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Profile download failure
                        assert response.errorBody() != null;
                        Log.d("ProfileService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Profile download success
                    Log.d("ProfileService", "res:" + response.message());

                    /* Change profile image */
                    InputStream stream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("ProfileService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
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
                    if (response.body().has("age")) {
                        age.setText(response.body().get("age").toString());
                    }
                    if (response.body().has("region")) {
                        String region_str = response.body().get("region").toString();
                        region.setText(region_str.substring(1, region_str.length() - 1));
                    }
                    if (response.body().has("height")) {
                        height.setText(response.body().get("height").toString());
                    }
                    if (response.body().has("job")) {
                        String job_str = response.body().get("job").toString();
                        job.setText(job_str.substring(1, job_str.length() - 1));
                    }
                    if (response.body().has("hobby")) {
                        String hobby_str = response.body().get("hobby").toString();
                        hobby.setText(hobby_str.substring(1, hobby_str.length() - 1));
                    }
                    if (response.body().has("smoke")) {
                        Boolean smoke_bol = Boolean.parseBoolean(response.body().get("smoke").toString());
                        if (smoke_bol) {
                            smoke.setText("흡연");
                        }
                        else {
                            smoke.setText("비흡연");
                        }
                    }
                    if (response.body().has("drink")) {
                        int drink_int = Integer.parseInt(response.body().get("drink").toString());
                        if (drink_int == 0) {
                            drink.setText("마시지 않음");
                        } else if (drink_int == 1) {
                            drink.setText("가끔 마심");
                        } else if (drink_int == 2) {
                            drink.setText("자주 마심");
                        } else if (drink_int == 3) {
                            drink.setText("즐겨 마심");
                        }
                    }
                    if (response.body().has("self_instruction")) {
                        String self_instruction_str = response.body().get("self_instruction").toString();
                        self_instruction.setText(self_instruction_str.substring(1, self_instruction_str.length() - 1));
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
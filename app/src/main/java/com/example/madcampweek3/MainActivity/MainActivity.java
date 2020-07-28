package com.example.madcampweek3.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek3.Account.AccountEditFragment;
import com.example.madcampweek3.Account.MyAccountActivity;
import com.example.madcampweek3.BluetoothService.BluetoothService;
import com.example.madcampweek3.LocalScan.LocalScan;
import com.example.madcampweek3.Profile.ProfileFragment;
import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.FriendService;
import com.example.madcampweek3.RetrofitService.ImageService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.example.madcampweek3.Utils.BackPressCloseHandler;
import com.example.madcampweek3.fragment.CenteredTextFragment;
import com.example.madcampweek3.menu.DrawerAdapter;
import com.example.madcampweek3.menu.DrawerItem;
import com.example.madcampweek3.menu.SimpleItem;
import com.example.madcampweek3.menu.SpaceItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.Integer.max;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_PROBABILITY = 3;
    private static final int POS_CART = 4;
    private static final int POS_LOGOUT = 5;
    public static final int ONGOING_BLUETOOTH = 1;
    public static final String PROFILE_IMAGE_NAME = "profile_image.jpg";
    public static final String PROFILE_IMAGE_KIND = "profile";
    SharedPreferences appData ;
    String userId = "";

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private TextView mProbability;
    private SlidingRootNav slidingRootNav;
    private CircleImageView myProfileImage;
    private TextView myUserName;
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //TODO: 확인 필요해
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(10);
        getSupportActionBar().setTitle("우연히 봄");

        /* Run bluetooth service */
        Intent intent = new Intent(this, BluetoothService.class);
        startService(intent);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_PROBABILITY),
                createItemFor(POS_CART),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);

        mProbability = findViewById(R.id.probability);
        myProfileImage = findViewById(R.id.my_profile_image);
        myUserName=findViewById(R.id.my_user_name);
        appData =getSharedPreferences("appData", MODE_PRIVATE);
        userId = appData.getString("ID","");

        myProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        getProbability();
        setUsernameInfo(userId);
        setProfileImage(myProfileImage,1);
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finish();
        }
        else if (position == POS_DASHBOARD)
        {
            slidingRootNav.closeMenu();
            Fragment selectedScreen = new ProfileFragment();
            showFragment(selectedScreen);
        } else if (position == POS_ACCOUNT) {
            slidingRootNav.closeMenu();
            Fragment selectedScreen = new AccountEditFragment();
            showFragment(selectedScreen);
        } else if (position == POS_PROBABILITY) { // TODO: Change POS_CART
            slidingRootNav.closeMenu();
            Fragment selectedScreen = new LocalScan();
            showFragment(selectedScreen);
        }
        else {
            slidingRootNav.closeMenu();
            Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
            showFragment(selectedScreen);
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }


    private void setUsernameInfo(String userID) {
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
                        String myUserName_str = response.body().get("userName").toString();
                        myUserName.setText((myUserName_str.substring(1, myUserName_str.length() - 1)));
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

    private void setProfileImage(CircleImageView imageView, int position) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ImageService service = retrofit.create(ImageService.class);

        /* Send image download request */
        String fileName = position + "_" + PROFILE_IMAGE_NAME;
        service.downloadProfile(userId, PROFILE_IMAGE_KIND, fileName).enqueue(new Callback<ResponseBody>() {
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

    private void getProbability() {
        /* Init retrofit */
        Retrofit retrofit = RetrofitClient.getInstnce();
        FriendService service = retrofit.create(FriendService.class);

        /* Prepare input */
        Calendar time = Calendar.getInstance();
        int date = time.get(Calendar.DAY_OF_YEAR) + (365 * time.get(Calendar.YEAR));

        /* Get today list */
        service.getTodayFriend(userId, date).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Failure
                        assert response.errorBody() != null;
                        Log.d("FriendService", "res: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // Success
                    Log.d("FriendService", "res: " + response.body().toString());

                    int score = getMaxIntimacy(response);
                    mProbability.setText(String.valueOf(score));
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d("FriendService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });

    }

    private int getMaxIntimacy(Response<JsonObject> response) {
        JsonArray intimacyScoreList = new JsonArray ();

        assert response.body() != null;
        if (response.body().has("intimacyScore")) {
            intimacyScoreList = response.body().getAsJsonArray("intimacyScore");
        }

        ArrayList<Number> revisedIntimacyScoreList = reviseIntimacyScore(intimacyScoreList);
        int maxScore = 0;
        for (int i = 0; i < intimacyScoreList.size(); ++i) {
            int intimacyScore = revisedIntimacyScoreList.get(i).intValue();
            maxScore = max(intimacyScore, maxScore);
        }
        return maxScore;

    }

    private ArrayList<Number> reviseIntimacyScore(JsonArray intimacyScore) {
        int totalScore = 0;
        ArrayList<Number> res = new ArrayList<>();
        for (JsonElement score: intimacyScore) {
            totalScore += score.getAsInt();
        }
        for (JsonElement score: intimacyScore) {
            res.add(score.getAsDouble() / totalScore * 100);
        }

        return res;
    }
}
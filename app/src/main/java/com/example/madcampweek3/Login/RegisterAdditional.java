package com.example.madcampweek3.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.example.madcampweek3.Utils.User;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class RegisterAdditional
        extends AppCompatActivity {
    private static final String TAG = "RegisterHobby";

    //User Info - Intent로 전달받는 것
    User userInfo;
    String password;
    private int height;
    private Context mContext;
    private String instruction, hobby;
    private EditText minstruction, mhobby;
    private Button smoke,nonsmoke,nondrink,drink,continueButton;
    private boolean smoking=true;
    private boolean drinking=true;
    private NumberPicker mheight;

    private String append = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_additional);
        mContext = RegisterAdditional.this;

        Log.d(TAG, "onCreate: started");

        Intent intent = getIntent();
        userInfo = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");

        initWidgets();

        init();
    }

    private void initWidgets() {
        minstruction = findViewById(R.id.register_self_instruction);
        
        mheight=findViewById(R.id.register_height);

        mheight.setMinValue(100);
        mheight.setMaxValue(210);
        mheight.setWrapSelectorWheel(false);

        mhobby=findViewById(R.id.register_hobby);


        smoke = findViewById(R.id.smokeSelectionButton);
        nonsmoke = findViewById(R.id.nonsmokeSelectionButton);
        drink = findViewById(R.id.drinkSelectionButton);
        nondrink = findViewById(R.id.nondrinkSelectionButton);
        continueButton = findViewById(R.id.btn_register_additional_next);

        smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smokeButtonClicked();
            }
        });

        nonsmoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonsmokeButtonClicked();
            }
        });

        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drinkButtonClicked();
            }
        });

        nondrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nondrinkButtonClicked();
            }
        });


    }

    public void smokeButtonClicked() {
        // this is to toggle between selection and non selection of button
        smoking = true;
        smoke.setBackgroundColor(getColor(R.color.mainColor));
        smoke.setAlpha(1.0f);
        nonsmoke.setAlpha(.5f);
        nonsmoke.setBackgroundColor(getColor(R.color.white));

    }

    public void nonsmokeButtonClicked() {
        // this is to toggle between selection and non selection of button
        smoking = false;
        nonsmoke.setBackgroundColor(getColor(R.color.mainColor));
        nonsmoke.setAlpha(1.0f);
        smoke.setAlpha(.5f);
        smoke.setBackgroundColor(getColor(R.color.white));

    }

    public void drinkButtonClicked() {
        // this is to toggle between selection and non selection of button
        drinking = true;
        drink.setBackgroundColor(getColor(R.color.mainColor));
        drink.setAlpha(1.0f);
        nondrink.setAlpha(.5f);
        nondrink.setBackgroundColor(getColor(R.color.white));


    }

    public void nondrinkButtonClicked() {
        // this is to toggle between selection and non selection of button
        drinking = false;
        nondrink.setBackgroundColor(getColor(R.color.mainColor));
        nondrink.setAlpha(1.0f);
        drink.setAlpha(.5f);
        drink.setBackgroundColor(getColor(R.color.white));

    }

    public void init() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get additional info
                instruction = minstruction.getText().toString();
                hobby = mhobby.getText().toString();
                height = mheight.getValue();

                userInfo.setDescription(instruction);
                userInfo.setHobby(hobby);
                userInfo.setHeight(height);

                //set smoke drink
                userInfo.setSmoking(smoking);
                userInfo.setDrinking(drinking);
                if(checkInputs(instruction,hobby)) {
                    Toast.makeText(mContext, "가입이 완료되었어요 :) 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    tryRegister();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            }
        });
    }


    private boolean checkInputs(String instruction,  String hobby) {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (instruction.equals("") || hobby.equals("")) {
            Toast.makeText(mContext, "모든 정보를 기입해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }



    private void tryRegister() {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        AccountService service = retrofit.create(AccountService.class);

        /* Get personal info */
        JsonObject body_account = new JsonObject();

        /* Get proflie info*/
        JsonObject body_profile = new JsonObject();

        /* TODO: Change input */
        body_account.addProperty("id", userInfo.getEmail());
        body_account.addProperty("password", password);
        body_account.addProperty("name", userInfo.getUsername());
        body_account.addProperty("phoneNumber", userInfo.getPhone_number());
        body_account.addProperty("macAddress", getMacAddress());


        /*insert profile info*/
        //userInfo에서 받아오는데, 아직 정보 코드 수정은 안함
        body_profile.addProperty("id",userInfo.getEmail());
        body_profile.addProperty("age",userInfo.getEmail());
        body_profile.addProperty("region",userInfo.getEmail());
        body_profile.addProperty("height",userInfo.getEmail());
        body_profile.addProperty("job",userInfo.getEmail());
        body_profile.addProperty("hobby",userInfo.getEmail());
        body_profile.addProperty("smoke",userInfo.getEmail());
        body_profile.addProperty("drink",userInfo.getEmail());
        body_profile.addProperty("self_instruction",userInfo.getEmail());


        /* Send register request */
        service.register(body_account).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Register Failure
                        Log.d("AccountService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // Register Success
                        Log.d("AccountService", "res:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("AccountService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });

        /* Send register request */
        service.updateProfile(body_profile).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Register Failure
                        Log.d("AccountService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // Register Success
                        Log.d("AccountService", "res:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("AccountService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });



    }



    private String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                macBytes[5] = (byte) (macBytes[5] - 1);
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();

                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }







}

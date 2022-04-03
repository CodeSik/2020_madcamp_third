package com.example.madcampweek3.Login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.PositionService;
import com.example.madcampweek3.Utils.GPS;
import com.example.madcampweek3.Utils.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Grocery App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class RegisterBasicInfo extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    GPS gps;
    private Context mContext;
    private String email, password, phonenumber, region;
    private EditText mEmail, mPassword, mPhonenumber;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private String append = "";

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private LocationManager locationManager;

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(this);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            /* init retrofit */
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PositionService service = retrofit.create(PositionService.class);
            String address = latitude + "," + longitude;

            service.findRegion(address, getResources().getString(R.string.api_key), "ko").enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                    if (response.body() == null) {
                        try { // Failure
                            assert response.errorBody() != null;
                            Log.d("PositionService", "res: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else { // Success
                        if (response.body().has("results")) {
                            JsonArray results = response.body().getAsJsonArray("results");
//                            Log.d("PositionService", "res:" + results.get(0));
                            if (results.get(0).getAsJsonObject().has("formatted_address")) {
                                region = results.get(0).getAsJsonObject().get("formatted_address").toString();
                                Calendar currentTime = Calendar.getInstance();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                    Log.d("AccountService", "Failed API call with call: " + call
                            + ", exception: " + t);
                }


            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerbasic_info);
        mContext = RegisterBasicInfo.this;
        Log.d(TAG, "onCreate: started");
        getPosition();
        gps = new GPS(getApplicationContext());

        initWidgets();
        init();
    }

    private void init() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                mPhonenumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

                phonenumber = mPhonenumber.getText().toString();
                if (checkInputs(email, password,phonenumber)) {
                    //find geo location
                    //find geo location
//                    Location location = gps.getLocation();
//                    double latitude = 37.349642;
//                    double longtitude = -121.938987;
//                    if (location != null) {
//                        latitude = location.getLatitude();
//                        longtitude = location.getLongitude();
//                    }
//                    Log.d("Location==>", longtitude + "   " + latitude);


                    Intent intent = new Intent(RegisterBasicInfo.this, RegisterProfileInfo.class);
                    User user = new User("", phonenumber, email, "", "","", "", 9, "", "", 0,region, "", "",true ,true);
                    intent.putExtra("password", password);
                    intent.putExtra("classUser", user);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void getPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000, 1, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, mLocationListener);
    }

    private boolean checkInputs(String email,  String password, String phonenumber) {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("") || password.equals("") || phonenumber.equals("")) {
            Toast.makeText(mContext, "모든 정보를 기입해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Below code checks if the email id is valid or not.
        if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "올바르지 않은 이메일입니다.", Toast.LENGTH_SHORT).show();
            return false;

        }


        return true;
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: initializing widgets");
        mEmail = findViewById(R.id.input_email);
        btnRegister = findViewById(R.id.btn_register_basic_next);
        mPassword = findViewById(R.id.input_password);
        mPhonenumber=findViewById(R.id.input_phonenumber);
        mContext = RegisterBasicInfo.this;

    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));

    }
}

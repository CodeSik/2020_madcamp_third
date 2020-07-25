package com.example.madcampweek3.Login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcampweek3.R;
import com.google.gson.JsonObject;

import java.io.IOException;

import RetrofitService.LoginService;
import RetrofitService.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private EditText login, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login);
        login = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLogin();
            }
        });
    }

    private void tryLogin() {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        LoginService service = retrofit.create(LoginService.class);

        /* Get ID & PW */
        JsonObject body = new JsonObject();
        body.addProperty("id", login.getText().toString());
        body.addProperty("password", password.getText().toString());

        /* Send ID & PW */
        service.login(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Login Failure
                        Log.d("LoginService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // Login Success
                        Log.d("LoginService", "res:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("LoginService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }

    private void tryRegister() {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        LoginService service = retrofit.create(LoginService.class);

        /* Get personal info */
        JsonObject body = new JsonObject();
        /* TODO: Change input */
        body.addProperty("id", "test12");
        body.addProperty("password", "test");
        body.addProperty("name", "SEO");
        body.addProperty("phoneNumber", "010-1111-1111");

        /* Send register request */
        service.register(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Register Failure
                        Log.d("LoginService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // Register Success
                        Log.d("LoginService", "res:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("LoginService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }

    private void tryWithdrawal() {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        LoginService service = retrofit.create(LoginService.class);

        /* Get personal info */
        JsonObject body = new JsonObject();
        /* TODO: Change input */
        String id = "test12";
        String password = "test";

        /* Send register request */
        service.withdrawal(id, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Withdrawal Failure
                        Log.d("LoginService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // Withdrawal Success
                        Log.d("LoginService", "res:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("LoginService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }
}

package com.example.madcampweek3.RetrofitService;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountService {
    @POST("/api/account/register")
    Call<ResponseBody> register(
            @Body JsonObject body
    );

    @POST("/api/account/login")
    Call<ResponseBody> login(
            @Body JsonObject body
    );

    @DELETE("/api/account/delete")
    Call<ResponseBody> withdrawal(
            @Query("id") String id,
            @Query("password") String password
    );

    @GET("/api/account/findUser")
    Call<JsonObject> findUser(
            @Query("macAddress") String address
    );

    @GET("/api/account/downloadProfile")
    Call<JsonObject> downloadProfile(
            @Query("id") String id
    );

    @POST("/api/image/updateProfile")
    Call<ResponseBody> updateProfile(
            @Body JsonObject body
    );

}

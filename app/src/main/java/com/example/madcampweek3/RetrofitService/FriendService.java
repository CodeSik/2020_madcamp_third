package com.example.madcampweek3.RetrofitService;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendService {
    @POST("/api/friend/addContact")
    Call<JsonObject> addContact(
            @Body JsonObject body
            );
    @GET("/api/friend/getContactID")
    Call<JsonObject> getContactID(
            @Query("id") String id,
            @Query("friendID") String friendID
    );
}

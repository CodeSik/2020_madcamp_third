package com.example.madcampweek3.RetrofitService;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface FriendService {
    @POST("/api/friend/addContact")
    Call<JsonObject> addContact(
            @Body JsonObject body
            );

    @GET("/api/friend/getIntimacy")
    Call<JsonObject> getIntimacy(
            @Query("id") String id,
            @Query("friendID") String friendID
    );

    @PUT("/api/friend/addBlockUser")
    Call<JsonObject> addBlockUser(
            @Body JsonObject body
    );

    @DELETE("/api/friend/deleteBlockUser")
    Call<JsonObject> deleteBlockUser(
            @Query("id") String id,
            @Query("unblockUserID") String unblockUserID
    );

    @GET("/api/friend/getContactID")
    Call<JsonObject> getContactID(
            @Query("id") String id,
            @Query("friendID") String friendID,
            @Query("date") int date
    );

    @POST("/api/friend/sendLike")
    Call<JsonObject> sendLike(
            @Body JsonObject body
    );

    @POST("/api/friend/sendStar")
    Call<JsonObject> sendStar(
            @Body JsonObject body
    );

    @GET("/api/friend/getTodayFriend")
    Call<JsonObject> getTodayFriend(
            @Query("id") String id,
            @Query("date") int date
    );

    @PUT("/api/friend/registerMatch")
    Call<JsonObject> registerMatch(
            @Body JsonObject body
    );
}

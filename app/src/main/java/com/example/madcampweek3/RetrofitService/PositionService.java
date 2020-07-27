package com.example.madcampweek3.RetrofitService;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PositionService {
    @GET("/maps/api/geocode/json")
    Call<JsonObject> findRegion(
            @Query("latlng") String latlng,
            @Query("key") String key
    );

}

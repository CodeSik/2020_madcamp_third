package com.example.madcampweek3.RetrofitService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstnce() {
        if (instance == null) {
            // TODO: Change baseUrl
            instance = new Retrofit.Builder()
                    .baseUrl("http://ssal.sparcs.org:44153/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}

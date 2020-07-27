package com.example.madcampweek3.RetrofitService;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImageService {
    @Multipart
    @POST("/api/image/uploadProfile")
    Call<ResponseBody> uploadProfile(
            @Query("id") String id,
            @Query("imageKind") String imageKind,
            @Part MultipartBody.Part image
    );

    @GET("/api/image/downloadProfile")
    Call<ResponseBody> downloadProfile(
            @Query("id") String id,
            @Query("imageKind") String imageKine,
            @Query("name") String fileName
    );
}

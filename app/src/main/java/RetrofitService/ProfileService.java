package RetrofitService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfileService {
    @Multipart
    @POST("/api/image/uploadProfile")
    Call<ResponseBody> uploadProfile(
            @Query("id") String id,
            @Query("imageKind") String imageKind
    );

    @GET("/api/image/downloadProfile")
    Call<ResponseBody> downloadProfile(
            @Query("id") String id
    );
}

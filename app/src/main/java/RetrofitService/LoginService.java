package RetrofitService;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
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

}

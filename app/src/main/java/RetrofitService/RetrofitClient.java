package RetrofitService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstnce() {
        if (instance == null) {
            // TODO: Change baseUrl
            instance = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.38:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}

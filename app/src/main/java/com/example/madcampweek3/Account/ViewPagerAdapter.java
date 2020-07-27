package com.example.madcampweek3.Account;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.ImageService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class ViewPagerAdapter extends PagerAdapter {
    public static final String PROFILE_IMAGE_NAME = "profile_image.jpg";
    public static final String PROFILE_IMAGE_KIND = "profile";
    private String userId;
    private SharedPreferences appData;
    private int[] images = {R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3};
    private LayoutInflater inflater;
    private Context context;

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.viewpageslide, container, false);
        appData = context.getSharedPreferences("appData", MODE_PRIVATE);
        userId = appData.getString("ID","");

        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);


        setProfileImage(imageView,position+1);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }


    private void setProfileImage(ImageView imageView, int position) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ImageService service = retrofit.create(ImageService.class);

        /* Send image download request */
        String fileName = position + "_" + PROFILE_IMAGE_NAME;
        service.downloadProfile(userId, PROFILE_IMAGE_KIND, fileName).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Profile download failure
                        assert response.errorBody() != null;
                        Log.d("ProfileService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Profile download success
                    Log.d("ProfileService", "res:" + response.message());

                    /* Change profile image */
                    InputStream stream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("ProfileService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }
}

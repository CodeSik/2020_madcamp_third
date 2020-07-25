package com.example.madcampweek3.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.madcampweek3.R;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import RetrofitService.LoginService;
import RetrofitService.ProfileService;
import RetrofitService.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountFragment extends Fragment {
    ImageView profileImage;
    public static final int PICK_IMAGE = 1;
    public static final String PROFILE_IMAGE_NAME = "profile_image.jpg";
    public static final String PROFILE_IMAGE_KIND = "profile";
    public static final String userID = "test"; // TODO: Change it

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        Button uploadButton = (Button) view.findViewById(R.id.uploadButton);

        /* Set profile image */
        setProfileImage();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        return view;
    }

    /* Select image from gallery. */
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                return;
            }
            try {
                /* Create temp profile image file */
                InputStream stream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                File storage = getContext().getCacheDir();
                File tempFile = new File(storage, PROFILE_IMAGE_NAME);
                try{
                    tempFile.createNewFile();
                    FileOutputStream out = new FileOutputStream(tempFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tryUploadProfileImage(tempFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /* Send profile image to server */
    private void tryUploadProfileImage(File image) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ProfileService service = retrofit.create(ProfileService.class);


        /* Prepare image */
        int pos = image.toString().lastIndexOf( "." );
        String ext = image.toString().substring( pos + 1 );
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/" + ext), image);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestFile);

        /* Send image upload request */
        service.uploadProfile(userID, PROFILE_IMAGE_KIND, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Profile upload failure
                        assert response.errorBody() != null;
                        Log.d("ProfileService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // Profile upload success
                        Log.d("ProfileService", "res:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setProfileImage();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("ProfileService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }

    /* Download profile image from server, and set profile */
    private void setProfileImage() {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ProfileService service = retrofit.create(ProfileService.class);

        /* Send image download request */
        service.downloadProfile(userID, PROFILE_IMAGE_KIND, PROFILE_IMAGE_NAME).enqueue(new Callback<ResponseBody>() {
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
                    profileImage.setImageBitmap(bitmap);
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

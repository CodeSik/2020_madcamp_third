package com.example.madcampweek3.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.ImageService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.example.madcampweek3.Utils.User;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterPhoto extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    public static final String PROFILE_IMAGE_NAME = "profile_image.jpg";
    public static final String PROFILE_IMAGE_KIND = "profile";
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int selectedImagePos = 0;
    private Button continueButton;

    User userInfo;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_photo);
        Intent intent = getIntent();
        userInfo = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");
        initWidgets();
        setInitProfileImage();
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView1;
                selectedImagePos = 1;
                proceedAfterPermission();

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView2;
                selectedImagePos = 2;
                proceedAfterPermission();

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView3;
                selectedImagePos = 3;
                proceedAfterPermission();

            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView4;
                selectedImagePos = 4;
                proceedAfterPermission();

            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView5;
                selectedImagePos = 5;
                proceedAfterPermission();

            }
        });
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView6;
                selectedImagePos = 6;
                proceedAfterPermission();

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();

            }
         });
    }

    private void setProfileImage(ImageView imageView, int position) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ImageService service = retrofit.create(ImageService.class);

        /* Send image download request */
        String fileName = position + "_" + PROFILE_IMAGE_NAME;
        service.downloadProfile(userInfo.getEmail(), PROFILE_IMAGE_KIND, fileName).enqueue(new Callback<ResponseBody>() {
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
    private void setInitProfileImage() {
        imageView = imageView1;
        selectedImagePos = 1;
        setProfileImage(imageView, selectedImagePos);

        imageView = imageView2;
        selectedImagePos = 2;
        setProfileImage(imageView, selectedImagePos);

        imageView = imageView3;
        selectedImagePos = 3;
        setProfileImage(imageView, selectedImagePos);

        imageView = imageView4;
        selectedImagePos = 4;
        setProfileImage(imageView, selectedImagePos);

        imageView = imageView5;
        selectedImagePos = 5;
        setProfileImage(imageView, selectedImagePos);

        imageView = imageView6;
        selectedImagePos = 6;
        setProfileImage(imageView, selectedImagePos);
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void proceedAfterPermission() {
        final CharSequence[] options = {"사진 찍기", "갤러리에서 선택", "취소"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPhoto.this);
        builder.setTitle("프로필 사진 설정");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("사진 찍기"))
                {
                    cameraIntent();
                } else if (options[item].equals("갤러리에서 선택"))
                {
                    galleryIntent();
                } else if (options[item].equals("취소")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void initWidgets() {
        imageView1 = findViewById(R.id.register_image_view_1);
        imageView2 = findViewById(R.id.register_image_view_2);
        imageView3 = findViewById(R.id.register_image_view_3);
        imageView4 = findViewById(R.id.register_image_view_4);
        imageView5 = findViewById(R.id.register_image_view_5);
        imageView6 = findViewById(R.id.register_image_view_6);
        continueButton=findViewById(R.id.btn_register_photo_next);
    }

    /* Send profile image to server */
    private void tryUploadProfileImage(File image) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ImageService service = retrofit.create(ImageService.class);


        /* Prepare image */
        int pos = image.toString().lastIndexOf( "." );
        String ext = image.toString().substring( pos + 1 );
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/" + ext), image);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestFile);

        /* Send image upload request */
        service.uploadProfile(userInfo.getEmail(), PROFILE_IMAGE_KIND, body).enqueue(new Callback<ResponseBody>() {
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
                    setProfileImage(imageView, selectedImagePos);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("ProfileService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE) {
            if (data == null) {
                return;
            }
            try {
                /* Create temp profile image file */
                InputStream stream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                File storage = getApplicationContext().getCacheDir();
                File tempFile = new File(storage, selectedImagePos + "_" + PROFILE_IMAGE_NAME);
                try {
                    tempFile.createNewFile();
                    FileOutputStream out = new FileOutputStream(tempFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tryUploadProfileImage(tempFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (data == null) {
                return;
            }
            /* Create temp profile image file */
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            File storage = getApplicationContext().getCacheDir();
            File tempFile = new File(storage, selectedImagePos + "_" + PROFILE_IMAGE_NAME);
            try {
                tempFile.createNewFile();
                FileOutputStream out = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tryUploadProfileImage(tempFile);
        }
    }
}
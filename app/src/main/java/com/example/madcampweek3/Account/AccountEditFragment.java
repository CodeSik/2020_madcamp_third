package com.example.madcampweek3.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.madcampweek3.MainActivity.MainActivity;
import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.ImageService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.example.madcampweek3.Utils.BackPressCloseHandler;
import com.google.gson.JsonObject;

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

import static android.content.Context.MODE_PRIVATE;

public class AccountEditFragment extends Fragment {
    SharedPreferences appData ;
    String userId = "";

    public static final int PICK_IMAGE = 1;
    public static final String PROFILE_IMAGE_NAME = "profile_image.jpg";
    public static final String PROFILE_IMAGE_KIND = "profile";


    Button smoking, nonsmoking, drinking, nondrinking, continueButton;

    ImageButton back;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView;

    EditText mself_instruction, mschool,mmajor,mjob,mhobby;
    String self_instruction, school, major, job, hobby;

    boolean smoke = true;
    boolean drink = true;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int selectedImagePos = 0;
    private int age;
    private String region;
    private int height;


    private void startMainActivity() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        appData =getContext().getSharedPreferences("appData", MODE_PRIVATE);
        userId = appData.getString("ID","");

        //위젯 초기화 --> 여기서 서버에서 받아온다음에 전부 설정해줘야함.
        initWidgets(view);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryUploadProfileInfo(); // TODO: Fix automatically profile image upload
                startMainActivity();
            }
        });
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


        return view;
    }

    private void initWidgets(View view) {
        Retrofit retrofit = RetrofitClient.getInstnce();
        AccountService service = retrofit.create(AccountService.class);


        imageView1 = view.findViewById(R.id.image_view_1);
        imageView2 = view.findViewById(R.id.image_view_2);
        imageView3 = view.findViewById(R.id.image_view_3);
        imageView4 = view.findViewById(R.id.image_view_4);
        imageView5 = view.findViewById(R.id.image_view_5);
        imageView6 = view.findViewById(R.id.image_view_6);
        continueButton = view.findViewById(R.id.edit_fragment_next);

        mself_instruction = view.findViewById(R.id.editfragment_self_instruction);
        mschool = view.findViewById(R.id.editfragment_school);
        mmajor = view.findViewById(R.id.editfragment_major);
        mjob = view.findViewById(R.id.editfragment_job);
        mhobby = view.findViewById(R.id.editfragment_hobby);
        smoking = view.findViewById(R.id.editfragment_smokeSelectionButton);
        nonsmoking= view.findViewById(R.id.editfragment_nonsmokeSelectionButton);
        drinking = view.findViewById(R.id.editfragment_drinkSelectionButton);
        nondrinking= view.findViewById(R.id.editfragment_nondrinkSelectionButton);


        smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smokeButtonClicked();
            }
        });
        nonsmoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonsmokeButtonClicked();
            }
        });
        drinking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drinkButtonClicked();
            }
        });
        nondrinking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nondrinkButtonClicked();
            }
        });

        service.downloadProfile(userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Profile download failure
                        assert response.errorBody() != null;
                        Log.d("LoginService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Profile download success
                    Log.d("LoginService", "res:" + response.message());

                    if (response.body().has("self_instruction")) {
                        String self_instruction_str = response.body().get("self_instruction").toString();
                        mself_instruction.setText(self_instruction_str.substring(1, self_instruction_str.length() - 1));
                    }

                    if (response.body().has("school")) {
                        String school_str = response.body().get("school").toString();
                        mschool.setText(school_str.substring(1, school_str.length() - 1));
                    }

                    if (response.body().has("major")) {
                        String major_str = response.body().get("major").toString();
                        mmajor.setText(major_str.substring(1, major_str.length() - 1));
                    }

                    if (response.body().has("job")) {
                        String job_str = response.body().get("job").toString();
                        mjob.setText(job_str.substring(1, job_str.length() - 1));
                    }
                    if (response.body().has("hobby")) {
                        String hobby_str = response.body().get("hobby").toString();
                        mhobby.setText(hobby_str.substring(1, hobby_str.length() - 1));
                    }

                    /* Change profile info */
                    if (response.body().has("age")) {
                        age = response.body().get("age").getAsInt();
                    }
                    if (response.body().has("region")) {
                        String region_str = response.body().get("region").toString();
                        region = (region_str.substring(1, region_str.length() - 1));
                    }
                    if (response.body().has("height")) {
                        height = response.body().get("height").getAsInt();
                    }

                    if (response.body().has("smoke")) {
                        Boolean smoke_bol = Boolean.parseBoolean(response.body().get("smoke").toString());
                        smoke = smoke_bol;
                    }
                    if (response.body().has("drink")) {
                        Boolean drink_bol = Boolean.parseBoolean(response.body().get("drink").toString());
                        drink = drink_bol;
                    }

                }
            }
                @Override
                public void onFailure (Call < JsonObject > call, Throwable t){
                    // Profile download success
                    Log.d("ProfileService", "Failed API call with call: " + call
                            + ", exception: " + t);

                }

        });
     }


    public void smokeButtonClicked() {
        // this is to toggle between selection and non selection of button
        smoke = true;
        smoking.setBackgroundColor(getContext().getColor(R.color.mainColor));
        smoking.setAlpha(1.0f);
        nonsmoking.setAlpha(.5f);
        nonsmoking.setBackgroundColor(getContext().getColor(R.color.white));

    }
    public void nonsmokeButtonClicked() {
        // this is to toggle between selection and non selection of button
        smoke = false;
        nonsmoking.setBackgroundColor(getContext().getColor(R.color.mainColor));
        nonsmoking.setAlpha(1.0f);
        smoking.setAlpha(.5f);
        smoking.setBackgroundColor(getContext().getColor(R.color.white));

    }
    public void drinkButtonClicked() {
        // this is to toggle between selection and non selection of button
        drink = true;
        drinking.setBackgroundColor(getContext().getColor(R.color.mainColor));
        drinking.setAlpha(1.0f);
        nondrinking.setAlpha(.5f);
        nondrinking.setBackgroundColor(getContext().getColor(R.color.white));
    }
    public void nondrinkButtonClicked() {
        // this is to toggle between selection and non selection of button
        drink = false;
        nondrinking.setBackgroundColor(getContext().getColor(R.color.mainColor));
        nondrinking.setAlpha(1.0f);
        drinking.setAlpha(.5f);
        drinking.setBackgroundColor(getContext().getColor(R.color.white));
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


        AlertDialog.Builder builder = new AlertDialog.Builder(AccountEditFragment.this.getActivity());

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




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == SELECT_FILE) {
            if (data == null) {
                return;
            }
            try {
                /* Create temp profile image file */
                InputStream stream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                File storage = getContext().getCacheDir();
                File tempFile = new File(storage, selectedImagePos + "_" + PROFILE_IMAGE_NAME);
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
        } else if (requestCode == REQUEST_CAMERA) {
            if (data == null) {
                return;
            }
            /* Create temp profile image file */
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            File storage = getContext().getCacheDir();
            File tempFile = new File(storage, selectedImagePos + "_" + PROFILE_IMAGE_NAME);
            try{
                tempFile.createNewFile();
                FileOutputStream out = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tryUploadProfileImage(tempFile);
        }
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
        service.uploadProfile(userId, PROFILE_IMAGE_KIND, body).enqueue(new Callback<ResponseBody>() {
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

    /* Upload profile image to server */
    private void tryUploadProfileInfo() {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        AccountService service = retrofit.create(AccountService.class);


        /* Prepare information */
        JsonObject body = new JsonObject();
        body.addProperty("id", userId);
        body.addProperty("age", age);
        body.addProperty("region", region);
        body.addProperty("height", height);

        //-------------------------------여기까지는 수정 불가 정보


        body.addProperty("job", mjob.getText().toString());
        body.addProperty("hobby", mhobby.getText().toString());
        body.addProperty("smoke", smoke);
        body.addProperty("drink",drink);
        body.addProperty("self_instruction",mself_instruction.getText().toString());
        body.addProperty("school",mschool.getText().toString());
        body.addProperty("major",mmajor.getText().toString());


        /* Send image upload request */
        service.updateProfile(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() == null) {
                    try { // Profile upload failure
                        assert response.errorBody() != null;
                        Log.d("AccountService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // Profile upload success
                        Log.d("AccountService", "res:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getContext(), "프로필이 성공적으로 업데이트 되었습니다.", Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("ProfileService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }

    /* Download profile image from server, and set profile */
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

}

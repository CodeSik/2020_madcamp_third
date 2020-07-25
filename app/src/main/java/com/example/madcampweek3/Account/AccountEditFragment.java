package com.example.madcampweek3.Account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.madcampweek3.MainActivity.MainActivity;
import com.example.madcampweek3.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

public class AccountEditFragment extends Fragment {
    public static final int PICK_IMAGE = 1;
    public static final String PROFILE_IMAGE_NAME = "profile_image.jpg";
    public static final String PROFILE_IMAGE_KIND = "profile";
    public static final String userID = "test"; // TODO: Change it


    private static final String TAG = "EditProfileActivity";
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    //firebase
    private static final int REQUEST_PERMISSION_SETTING = 101;
    Button man, woman;
    ImageButton back;
    TextView man_text, women_text;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView;
    Bitmap myBitmap;
    Uri picUri;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext = AccountEditFragment.this.getContext();
    private ImageView mProfileImage;
    private String userId, profileImageUri;
    private Uri resultUri;
    private String userSex;
    private EditText phoneNumber, aboutMe;
    private CheckBox sportsCheckBox, travelCheckBox, musicCheckBox, fishingCheckBox;
    private boolean isSportsClicked = false;
    private boolean isTravelClicked = false;
    private boolean isFishingClicked = false;
    private boolean isMusicClicked = false;
    private RadioGroup userSexSelection;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private int selectedImagePos = 0;


    private void startMainActivity() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        permissionStatus = getContext().getSharedPreferences("permissionStatus", getContext().MODE_PRIVATE);
        requestMultiplePermissions();
        imageView1 = view.findViewById(R.id.image_view_1);
        imageView2 = view.findViewById(R.id.image_view_2);
        imageView3 = view.findViewById(R.id.image_view_3);
        imageView4 = view.findViewById(R.id.image_view_4);
        imageView5 = view.findViewById(R.id.image_view_5);
        imageView6 = view.findViewById(R.id.image_view_6);
        man = view.findViewById(R.id.smoking_button);
        woman = view.findViewById(R.id.nonsmoking_button);
        man_text = view.findViewById(R.id.smoking_text);
        women_text = view.findViewById(R.id.nonsmoking_text);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        woman.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                women_text.setTextColor(R.color.colorAccent);
                woman.setBackgroundResource(R.drawable.ic_check_select);
                man_text.setTextColor(R.color.black);
                man.setBackgroundResource(R.drawable.ic_check_unselect);
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                man_text.setTextColor(R.color.colorAccent);
                man.setBackgroundResource(R.drawable.ic_check_select);
                women_text.setTextColor(R.color.black);
                woman.setBackgroundResource(R.drawable.ic_check_unselect);
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
    private void requestMultiplePermissions() {
        if (ActivityCompat.checkSelfPermission(AccountEditFragment.this.getContext(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AccountEditFragment.this.getContext(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AccountEditFragment.this.getContext(), permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AccountEditFragment.this.getActivity(), permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AccountEditFragment.this.getActivity(), permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AccountEditFragment.this.getActivity(), permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountEditFragment.this.getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AccountEditFragment.this.getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountEditFragment.this.getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                       // Toast.makeText(getContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(AccountEditFragment.this.getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            // txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            //proceedAfterPermission();
        }
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

    /* Download profile image from server, and set profile */
    private void setProfileImage(ImageView imageView, int position) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        ProfileService service = retrofit.create(ProfileService.class);

        /* Send image download request */
        String fileName = position + "_" + PROFILE_IMAGE_NAME;
        service.downloadProfile(userID, PROFILE_IMAGE_KIND, fileName).enqueue(new Callback<ResponseBody>() {
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

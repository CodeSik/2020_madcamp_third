package com.example.madcampweek3.Intro;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madcampweek3.MainActivity.MainActivity;
import com.example.madcampweek3.R;
import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {

    Fragment mSplash0 = new SplashFragmentPermission();
    Fragment mSplash1 = new SplashFragment1();
    Fragment mSplash2 = new SplashFragment2();
    Fragment mSplash3 = new SplashFragment3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);  // 화면위 타이틀 없애기

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        addSlide(mSplash0);
        addSlide(mSplash1);
        addSlide(mSplash2);
        addSlide(mSplash3);

        setIndicatorColor(
                selectedIndicatorColor=getColor(R.color.selected),
                unselectedIndicatorColor=getColor(R.color.unselected)
        );
        setFadeAnimation();
//        setSlideOverAnimation();
        //흰색
        int white = getResources().getColor(R.color.colorPrimary);

        setBarColor(white);

        setImmersiveMode(true);

        showSkipButton(false);

        showStatusBar(false);

        //검은색
        int black = getResources().getColor(R.color.textColorPrimary);

        setColorSkipButton(black);
        setColorDoneText(black);
        setSeparatorColor(white);
        setNextArrowColor(black);



        String[] permissionList = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS};

        askForPermissions(permissionList,1);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startMainActivity();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment,
                               @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startMainActivity() {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
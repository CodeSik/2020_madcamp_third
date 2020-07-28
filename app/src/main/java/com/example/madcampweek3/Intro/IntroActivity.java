package com.example.madcampweek3.Intro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madcampweek3.Login.Login;
import com.example.madcampweek3.R;
import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {


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




    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startLoginActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startLoginActivity();
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

    private void startLoginActivity() {
        Intent intent = new Intent(IntroActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}
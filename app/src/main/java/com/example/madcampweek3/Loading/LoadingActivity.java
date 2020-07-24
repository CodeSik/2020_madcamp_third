package com.example.madcampweek3.Loading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.madcampweek3.Intro.IntroActivity;
import com.example.madcampweek3.R;

public class LoadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        ImageView splashGif = (ImageView)findViewById(R.id.gif_image);
        Glide.with(this)
                .load(R.raw.loadinggif)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(splashGif));

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3500); // 1초 후에 hd handler 실행  3000ms = 3초

    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), IntroActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
            LoadingActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }


  }

package com.example.madcampweek3.Account;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.madcampweek3.R;
import com.google.android.material.appbar.AppBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    @BindView(R.id.toolbar_header_view)
    protected HeaderView toolbarHeaderView;

    @BindView(R.id.float_header_view)
    protected HeaderView floatHeaderView;

    @BindView(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private boolean isHideToolbarView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUi();
    }

    private void initUi() {
        appBarLayout.addOnOffsetChangedListener(this);

        //TODO: 서버의 데이터로 바꿔야함
        toolbarHeaderView.bindTo("Larry Page", "Last seen today at 7.00PM");
        floatHeaderView.bindTo("Larry Page", "Last seen today at 7.00PM");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }


}
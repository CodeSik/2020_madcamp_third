package com.example.madcampweek3.Account;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madcampweek3.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by anton on 11/12/15.
 */

public class HeaderView extends LinearLayout {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.last_seen)
    TextView lastSeen;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String name, String lastSeen) {
        //TODO: 서버의 데이터로 바꿔야함
        this.name.setText(name);
        this.lastSeen.setText(lastSeen);
    }

    public void setTextSize(float size) {
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}

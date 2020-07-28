package com.example.madcampweek3.LocalScan;

import android.graphics.Bitmap;

public class Friend {
    String name;
    Number score;
    String phoneNumber;
    Bitmap profile;

    public Friend(String name, Number score, String phoneNumber) {
        this.name = name;
        this.score = score;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
    }

    public void setProfile(Bitmap profile) {
        this.profile = profile;
    }


}

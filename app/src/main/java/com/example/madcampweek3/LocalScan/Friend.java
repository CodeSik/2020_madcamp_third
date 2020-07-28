package com.example.madcampweek3.LocalScan;

import android.graphics.Bitmap;

public class Friend {
    String id;
    String name;
    Number score;
    String phoneNumber;
    Bitmap profile;

    public Friend(String id, String name, Number score, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.phoneNumber = phoneNumber;
    }

    public void setProfile(Bitmap profile) { this.profile = profile; }
    public String getId() { return this.id; }


}

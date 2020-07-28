package com.example.madcampweek3.LocalScan;

import android.graphics.Bitmap;

public class Pending {
    String id;
    String name;
    Bitmap profile;

    public Pending(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setProfile(Bitmap profile) { this.profile = profile; }

    public String getId() { return this.id; }
}

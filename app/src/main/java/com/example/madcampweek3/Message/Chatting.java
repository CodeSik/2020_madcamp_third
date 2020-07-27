package com.example.madcampweek3.Message;

import android.graphics.drawable.Drawable;

public class Chatting {
    private Drawable iconDrawable ;
    private String name ;
    private String phoneNumber;
    private int probability;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setName(String title) {
        name = title ;
    }
    public void setPhoneNumber(String desc) {
        phoneNumber = desc ;
    }
    public void setProbability(int desc) {
        probability = desc ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getName() {
        return this.name ;
    }
    public String getPhoneNumber() {
        return this.phoneNumber ;
    }
    public int getProbability(){
        return this.probability;
    }
}

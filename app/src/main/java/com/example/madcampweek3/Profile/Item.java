package com.example.madcampweek3.Profile;

import android.accounts.Account;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.FriendService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Simple POJO model for example
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Item {

    private String id;
    private String name;
    private String fromAddress;
    private String toAddress;
    private String contactTime;
    private Bitmap profile;

    private View.OnClickListener requestBtnClickListener;

    public Item() {
    }

    public Item(
            String id, String name,
            String fromAddress, String toAddress,
            String contactTime) {
        this.id = id;
        this.name = name;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.contactTime = contactTime;
        this.profile = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }

    public Bitmap getProfile() {
        return profile;
    }

    public void setProfile(Bitmap profile) { this.profile = profile; }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (fromAddress != null ? fromAddress.hashCode() : 0);
        result = 31 * result + (toAddress != null ? toAddress.hashCode() : 0);
        result = 31 * result + (contactTime != null ? contactTime.hashCode() : 0);
        return result;
    }

}

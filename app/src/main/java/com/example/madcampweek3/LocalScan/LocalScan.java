package com.example.madcampweek3.LocalScan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek3.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class LocalScan extends Fragment {
    public static final int REQUEST_ENABLE_BT = 1;

    private RecyclerView recyclerView;
    private FriendAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_localscan, container, false);

        /* Set recycler view */
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_list);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FriendAdapter();
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /* Register for broadcasts when a device is discovered */
        super.onCreate(savedInstanceState);

        SharedPreferences appData = getContext().getSharedPreferences("appData", MODE_PRIVATE);
        userID = appData.getString("ID","");
        downloadMatchingList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void downloadMatchingList() {
        /* Init retrofit */
        Retrofit retrofit = RetrofitClient.getInstnce();
        AccountService service = retrofit.create(AccountService.class);

        /* Get matching list */
        service.getMatch(userID).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Failure
                        assert response.errorBody() != null;
                        Log.d("AccountService", "res:" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // Success
                    setMatchingList(response);
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d("AccountService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }

    private void setMatchingList(Response<JsonObject> response) {
        JsonArray friendNameList = new JsonArray ();
        JsonArray intimacyScoreList = new JsonArray ();

        if (response.body().has("friendName")) {
            friendNameList = response.body().getAsJsonArray("friendName");
        }
        if (response.body().has("intimacyScore")) {
            intimacyScoreList = response.body().getAsJsonArray("intimacyScore");
        }

        ArrayList<Number> revisedIntimacyScoreList = reviseIntimacyScore(intimacyScoreList);

        for (int i = 0; i < friendNameList.size(); ++i) {
            String friendName = friendNameList.get(i).toString();
            friendName = friendName.substring(1, friendName.length() - 1);
            int intimacyScore = revisedIntimacyScoreList.get(i).intValue();
            mAdapter.addFriend(new Friend(friendName, intimacyScore));
        }
    }

    private ArrayList<Number> reviseIntimacyScore(JsonArray intimacyScore) {
        int totalScore = 0;
        ArrayList<Number> res = new ArrayList<>();
        for (JsonElement score: intimacyScore) {
            totalScore += score.getAsInt();
        }
        for (JsonElement score: intimacyScore) {
            res.add(score.getAsDouble() / totalScore * 100);
        }

        return res;
    }
}

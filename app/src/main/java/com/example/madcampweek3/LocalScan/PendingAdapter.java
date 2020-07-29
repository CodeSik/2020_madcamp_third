package com.example.madcampweek3.LocalScan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.FriendService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.madcampweek3.MainActivity.MainActivity.userId;


public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder>{
    private List<View> mViewList = new ArrayList<>();
    private List<Pending> mDataset = new ArrayList<>();

    public static class PendingViewHolder extends RecyclerView.ViewHolder {
        public String friendID;
        public TextView name;
        public ImageView profile;
        public Button denyButton, allowButton;

        public PendingViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.friend_name);
            profile = (ImageView) v.findViewById(R.id.profile_image);
            allowButton = (Button) v.findViewById(R.id.allow_button);
            denyButton = (Button) v.findViewById(R.id.deny_button);

            allowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendDeny();
                    sendAllow();

                }
            });

            denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendDeny();

                }
            });
        }

        public void bind(Pending pending) {
            name.setText(pending.name);
            profile.setImageBitmap(pending.profile);
            friendID = pending.getId();
        }

        private void sendAllow() {
            /* Init retrofit */
            Retrofit retrofit = RetrofitClient.getInstnce();
            FriendService service = retrofit.create(FriendService.class);

            /* Get matching list */
            JsonObject body = new JsonObject();
            body.addProperty("id", userId);
            body.addProperty("friendID", this.friendID);
            service.registerMatch(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                    if (response.body() == null) {
                        try { // Failure
                            assert response.errorBody() != null;
                            Log.d("FriendService", "res:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else { // Success
                        Log.d("FriendService", "delete like success");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                    Log.d("FriendService", "Failed API call with call: " + call
                            + ", exception: " + t);
                }
            });

            JsonObject body2 = new JsonObject();
            body2.addProperty("id", this.friendID);
            body2.addProperty("friendID", userId);
            service.registerMatch(body2).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                    if (response.body() == null) {
                        try { // Failure
                            assert response.errorBody() != null;
                            Log.d("FriendService", "res:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else { // Success
                        Log.d("FriendService", "delete like success");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                    Log.d("FriendService", "Failed API call with call: " + call
                            + ", exception: " + t);
                }
            });
        }

        private void sendDeny() {
            /* Init retrofit */
            Retrofit retrofit = RetrofitClient.getInstnce();
            FriendService service = retrofit.create(FriendService.class);

            /* Get matching list */
            service.deleteLike(userId, this.friendID).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                    if (response.body() == null) {
                        try { // Failure
                            assert response.errorBody() != null;
                            Log.d("FriendService", "res:" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else { // Success
                        Log.d("FriendService", "delete like success");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                    Log.d("FriendService", "Failed API call with call: " + call
                            + ", exception: " + t);
                }
            });
        }
    }

    public PendingAdapter() {  }

    @Override
    public PendingAdapter.PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_view, parent, false);
        return new PendingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {
        if (!mViewList.contains(holder.itemView)) {
            mViewList.add(holder.itemView);
        }
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() { return mDataset.size(); }

    public void setPending(List<Pending> pending) {
        this.mDataset = pending;
        notifyDataSetChanged();
    }
    public void clear(){
        this.mDataset.clear();
        notifyDataSetChanged();
    }


}

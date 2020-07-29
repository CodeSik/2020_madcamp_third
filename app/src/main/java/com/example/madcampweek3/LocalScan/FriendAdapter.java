package com.example.madcampweek3.LocalScan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek3.R;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    public Context getContext() {
        return context;
    }

    private Context context;
    private List<View> mViewList = new ArrayList<>();

    public List<View> getmViewList() {
        return mViewList;
    }

    public List<Friend> getmDataset() {
        return mDataset;
    }

    private List<Friend> mDataset = new ArrayList<>();

    public class FriendViewHolder extends  RecyclerView.ViewHolder {
        public TextView name;
        public TextView score;
        public TextView phoneNumber;
        public ImageView image;

        private static final int PERMISSIONS_REQUEST_SEND_SMS = 1;

        public FriendViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.friend_name);
            score = (TextView) v.findViewById(R.id.intimacy_score);
            phoneNumber = (TextView) v.findViewById(R.id.phone_number);
            image = v.findViewById(R.id.friend_image);
            context = FriendAdapter.this.getContext();
        }

        public void bind(Friend friend) {
            name.setText(friend.name);
            score.setText(friend.score.toString());
            phoneNumber.setText(friend.phoneNumber);
            image.setImageBitmap(friend.profile);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent text = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + friend.phoneNumber));
                        context.startActivity(text);

                }
            });
        }
    }

    public FriendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FriendAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view, parent, false);



        return new FriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        if (!mViewList.contains(holder.itemView)) {
            mViewList.add(holder.itemView);
        }
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setFriend(List<Friend> friend) {
        this.mDataset = friend;
        notifyDataSetChanged();
    }
    public void clear()
    {
        this.mDataset.clear();
        notifyDataSetChanged();
    }
}

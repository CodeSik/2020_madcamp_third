package com.example.madcampweek3.LocalScan;

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
    private List<View> mViewList = new ArrayList<>();
    private List<Friend> mDataset = new ArrayList<>();

    public static class FriendViewHolder extends  RecyclerView.ViewHolder {
        public TextView name;
        public TextView score;
        public TextView phoneNumber;
        public ImageView image;
        public FriendViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.friend_name);
            score = (TextView) v.findViewById(R.id.intimacy_score);
            phoneNumber = (TextView) v.findViewById(R.id.phone_number);
            image = v.findViewById(R.id.friend_image);
        }

        public void bind(Friend friend) {
            name.setText(friend.name);
            score.setText(friend.score.toString());
            phoneNumber.setText(friend.phoneNumber);
            image.setImageBitmap(friend.profile);
        }
    }

    public FriendAdapter() { }

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

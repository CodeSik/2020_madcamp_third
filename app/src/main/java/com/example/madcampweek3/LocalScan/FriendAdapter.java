package com.example.madcampweek3.LocalScan;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public FriendViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.friend_name);
        }

        public void bind(Friend friend) {
            name.setText(friend.name);
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

    public void addFriend(Friend friend) {
        this.mDataset.add(friend);
        notifyDataSetChanged();
    }
}

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

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder>{
    private List<View> mViewList = new ArrayList<>();
    private List<Pending> mDataset = new ArrayList<>();

    public static class PendingViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView profile;

        public PendingViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.friend_name);
            profile = (ImageView) v.findViewById(R.id.profile_image);
        }

        public void bind(Pending pending) {
            name.setText(pending.name);
            profile.setImageBitmap(pending.profile);
        }
    }

    public PendingAdapter() { }

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


}

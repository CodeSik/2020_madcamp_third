package com.example.madcampweek3.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek3.LocalScan.FriendAdapter;
import com.example.madcampweek3.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private ArrayList<Chatting> mData = null;
    public MessageAdapter(ArrayList<Chatting> list) {
        super();
        mData = list;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_chat, parent, false) ;
        MessageAdapter.ViewHolder vh = new MessageAdapter.ViewHolder(view) ;
        return vh ;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        Chatting item = mData.get(position) ;
        holder.icon.setImageDrawable(item.getIcon()) ;
        holder.name.setText(item.getName()) ;
        holder.phoneNumber.setText(item.getPhoneNumber()) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon ;
        TextView name ;
        TextView phoneNumber ;

        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            icon = itemView.findViewById(R.id.chat_image) ;
            name = itemView.findViewById(R.id.chat_name) ;
            //phoneNumber = itemView.findViewById(R.id.chat_phoneNumber) ;
        }
    }
}

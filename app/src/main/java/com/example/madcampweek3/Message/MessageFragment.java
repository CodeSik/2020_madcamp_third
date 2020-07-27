package com.example.madcampweek3.Message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcampweek3.Account.AccountActivity;
import com.example.madcampweek3.Profile.FoldingCell;
import com.example.madcampweek3.Profile.FoldingCellListAdapter;
import com.example.madcampweek3.Profile.Item;
import com.example.madcampweek3.R;
import com.example.madcampweek3.Utils.User;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    RecyclerView mReyclerView = null;
    MessageAdapter adapter = null;
    ArrayList<Chatting> mList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);

        mReyclerView = view.findViewById(R.id.chatting_list);
        // 객체 지정



        adapter = new MessageAdapter(mList);
        mReyclerView.setAdapter(adapter);

        mReyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mReyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }


}

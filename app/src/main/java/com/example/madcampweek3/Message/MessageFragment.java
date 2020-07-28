package com.example.madcampweek3.Message;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.ID;

public class MessageFragment extends Fragment {
    RecyclerView mReyclerView = null;
    MessageAdapter adapter = null;
    ArrayList<Chatting> mList = new ArrayList<>();
    SharedPreferences appData;
    String userId = "";
    Socket socket;
    private io.socket.client.Socket mSocket;


    private ServerSocket serverSocket;
    ArrayList<String> user_list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);


        SocketAddress addr = new InetSocketAddress("192.168.1.1", 3333/*port*/) ;
        appData = getContext().getSharedPreferences("appData", MODE_PRIVATE);
        userId = appData.getString("ID", "");



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

    void sendID_practice() {
        JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("ID",ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("practice", jsonData);
    }


    void setSocket() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            String url = "http://192.249.19.244:2180";
            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        setPvpListening();
    }

    private void setPvpListening() {
        // listen for game finish

        mSocket.on("fight", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    String counterpart, counterpart_win, counterpart_lose;

                    JSONObject messageJson = new JSONObject(args[0].toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}



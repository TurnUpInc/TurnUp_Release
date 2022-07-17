package com.hkxps17.turnup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextChatBox;
    private ChatAdapter chatAdapter;
    private ArrayList<String> mChatList = new ArrayList<>();
    private Socket mSocket;

//    //To Integrate
//    String userId = "91ea68724e1b8";
//    String eventId = "EventA";
//    String userName = "Bro";

    String emailID = "";
    String eventId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Set<String> retS = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this)
                .getStringSet("id", new HashSet<String>());
        List<String> retL = new ArrayList<String>(retS);
        emailID = retL.get(0);

        Intent intent = ChatActivity.this.getIntent();

        eventId = intent.getStringExtra("EventTitle");


        RecyclerView mRecyclerViewChat = findViewById(R.id.rv_chat);
        mEditTextChatBox = findViewById(R.id.et_chat_box); // EditText
        Button mSendButton = findViewById(R.id.btn_send); // Button
        mSendButton.setOnClickListener(this);
        try {
            mSocket = IO.socket("https://turnup-chat.herokuapp.com/");  //http://192.168.29.50:3000
        } catch (URISyntaxException e) {
            Log.d("SOCKETERROR", e.toString());
        }
        mSocket.connect();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewChat.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(mChatList);
        mRecyclerViewChat.setAdapter(chatAdapter);
        connectUser();
        mSocket.on("output"+eventId, onNewMessageView);
        mSocket.on("message", onNewMessage);
    }

    private void connectUser() {
        mSocket.emit("connectUser", emailID);
        mSocket.emit("getChatHistory", eventId);
//        mSocket.emit("joinRoom", eventId);
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String message = mEditTextChatBox.getText().toString().trim();
                if (message != null && !TextUtils.isEmpty(message)) {
                    //mChatList.add(message);
                    mEditTextChatBox.setText("");
                    if (chatAdapter != null)
                        chatAdapter.notifyDataSetChanged();
                }
                mSocket.emit("chatMessage", eventId, message);
                break;
            default:
                Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private Emitter.Listener onNewMessageView = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    JSONArray messageHistory = (JSONArray) args[0];
                    mChatList.clear();
                    for (int i = 0; i < messageHistory.length(); i++) {
                        JSONObject message = messageHistory.optJSONObject(i);
                        String messageText = message.optString("text");
                        String username = message.optString("username");
                        String time = message.optString("time");
                        mChatList.add(username + " (" + time + "):  " + messageText);
                        if (chatAdapter != null)
                            chatAdapter.notifyDataSetChanged();
                    }

                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message = data.optString("text");
                    String username = data.optString("username");
                    String time = data.optString("time");
                    mChatList.add(username + " (" + time + "):  " + message);
                    if (chatAdapter != null)
                        chatAdapter.notifyDataSetChanged();
                }
            });
        }
    };

}
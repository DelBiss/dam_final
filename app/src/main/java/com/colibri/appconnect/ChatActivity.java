package com.colibri.appconnect;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.colibri.appconnect.data.entity.ChatRoom;
import com.colibri.appconnect.data.firestore.document.ChatDoc;
import com.colibri.appconnect.data.firestore.document.MessageDoc;
import com.colibri.appconnect.data.repository;
import com.colibri.appconnect.databinding.ActivityChatBinding;
import com.colibri.appconnect.util.QueryStatus;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    public static final String USERTO = "userTo";
    private static final String TAG = "ChatActivity";

    private boolean exist = false;
    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!getIntent().hasExtra(USERTO)) {
            finish();
        }

        ChatRoomListAdapter chatRoomListAdapter = new ChatRoomListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChatMessageHistory.setLayoutManager(linearLayoutManager);
        binding.rvChatMessageHistory.setAdapter(chatRoomListAdapter);



        checkIfExistChatRoom();

        binding.btnChatSend.setOnClickListener(v -> {
                if(!exist){
                    repository.getInstance().addChatroom(new ChatDoc(buildChatChannel(getIntent().getStringExtra(USERTO))),
                            new MessageDoc(binding.etChatMessage.getText().toString(), "userToId")
                            ,()->{
                        setAdapter(buildChatChannel(getIntent().getStringExtra(USERTO)));
                        binding.etChatMessage.setText("");
                    });
                }
                else{
                    String userFromId = FirebaseAuth.getInstance().getUid();
                    repository.getInstance().getChatroom(buildChatChannel(getIntent().getStringExtra(USERTO))).observe(this, test -> {
                        if(test.isSuccessful()){
                            test.getData()
                                    .sendMessage(new MessageDoc(binding.etChatMessage.getText().toString(), userFromId), null);
                            binding.etChatMessage.setText("");
                            binding.rvChatMessageHistory.scrollToPosition(binding.rvChatMessageHistory.getAdapter().getItemCount());
                        }
                    });


                    Log.e(TAG, "onCreate: else" );
                }
            });

    }

    private void setAdapter(String chatRoomid) {
        LiveData<QueryStatus<ChatRoom>> chatroom = repository.getInstance().getChatroom(chatRoomid);
        chatroom.observe(this, chatRoomQueryStatus -> {
            if (chatRoomQueryStatus.isSuccessful()) {
                chatRoomQueryStatus.getData().getLiveMessages().observe(this, messages -> {
                    if (messages.isSuccessful()) {
                        ((ChatRoomListAdapter) binding.rvChatMessageHistory.getAdapter()).submitList(Objects.requireNonNull(messages.getData()));
                        exist = true;
                    }
                });
            }
        });
    }

    private void checkIfExistChatRoom() {
        String chatRoomId = buildChatChannel(getIntent().getStringExtra(USERTO));
        repository.getInstance().getChatroom(chatRoomId).observe(this, chatRoom ->{
            if(chatRoom.getData() != null && chatRoom.isSuccessful()){
                setAdapter(buildChatChannel(getIntent().getStringExtra(USERTO)));
            }
        });
    }

    private String buildChatChannel(String userTo){

        String chatChannel = "";

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG, "buildChatChannel: " +user);
        int result = userTo.compareTo(user);

        if(result < 0){
            chatChannel = userTo + "_" + user;
        }else{
            chatChannel = user + "_" + userTo;
        }
        return chatChannel;
    }
}
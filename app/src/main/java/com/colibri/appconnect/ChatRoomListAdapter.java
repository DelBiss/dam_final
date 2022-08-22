package com.colibri.appconnect;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.colibri.appconnect.contactList.ContactClickListener;
import com.colibri.appconnect.data.entity.User;
import com.colibri.appconnect.data.firestore.document.MessageDoc;
import com.colibri.appconnect.databinding.MessageItemBinding;
import com.colibri.appconnect.databinding.UserItemBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class ChatRoomListAdapter extends ListAdapter<MessageDoc, ChatRoomListAdapter.ContactHolder> {

    ContactClickListener contactClickListener;
    boolean isFromMe = false;
    protected ChatRoomListAdapter() {
        super(MessageDoc.diffCallback);
    }


    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        MessageDoc doc = getItem(position);
        Log.d(TAG, "onBindViewHolder: Pos:" + position + " item: " + doc);
        holder.bind(doc);
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        private final MessageItemBinding binding;

        ContactHolder(View itemView){
            super(itemView);
            binding = MessageItemBinding.bind(itemView);
        }
        void bind(MessageDoc abb){
            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(abb.getUserToId())) {
                isFromMe = true;
            }
            binding.setIsFromMe(isFromMe);
            binding.setMessageDoc(abb);

        }
    }

    private static final String TAG = "AP::ActionButtonAdapter";
}
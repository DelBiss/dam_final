package com.colibri.appconnect;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colibri.appconnect.data.entity.User;
import com.colibri.appconnect.databinding.UserItemBinding;

import java.util.List;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.CategoryViewHolder> {

    private List<User> userList;
    private OnItemClickListener listener;


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        UserItemBinding binding;

        public CategoryViewHolder(@NonNull UserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind( int position, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(position));
        }

        public void setCategory(User user) {
            binding.setUser(user);
        }
    }

    public CustomAdaptor(List<User> clientsList, OnItemClickListener listener) {
        this.userList = clientsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomAdaptor.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomAdaptor.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(position, listener);
        holder.setCategory(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addConversation(User user){
        userList.add(0, user);
        notifyItemChanged(0);
    }


    public User getUser(int position){
        return userList.get(position);
    }

    public void updateUser(User user, int position){
        userList.set(position, user);
        notifyItemChanged(position);
    }

}

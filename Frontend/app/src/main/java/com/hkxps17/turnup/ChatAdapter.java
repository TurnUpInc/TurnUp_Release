package com.hkxps17.turnup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hkxps17.turnup.databinding.ItemChatBoxBinding;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<String> mChatList;

    public ChatAdapter(ArrayList<String> mChatList) {
        this.mChatList = mChatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_chat_box, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.mTextViewLeft.setText(mChatList.get(position));
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewLeft;
        public ChatViewHolder (@NonNull View itemView) {
            super (itemView);
            mTextViewLeft = (TextView) itemView.findViewById(R.id.tv_chat_message_left);
        }
    }
}
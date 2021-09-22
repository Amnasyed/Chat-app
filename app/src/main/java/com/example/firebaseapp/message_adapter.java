package com.example.firebaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class message_adapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int item_send=1;
    int item_recieve=2;

    public message_adapter(Context context,ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == item_send) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new RecieverViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages=messagesArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder senderViewHolder=(SenderViewHolder) holder;
            senderViewHolder.textmsg.setText(messages.getMsg());
        }
        else {

            RecieverViewHolder rcViewHolder=(RecieverViewHolder) holder;
            rcViewHolder.textmsg2.setText(messages.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return item_send;
        }
        else {
            return item_recieve;
        }
    }
    //sender viewholder

    class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView textmsg;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textmsg=itemView.findViewById(R.id.txtmsgs);
        }
    }
    class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView textmsg2;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textmsg2=itemView.findViewById(R.id.txtmsgs1);
        }
    }
}

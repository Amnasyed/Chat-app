package com.example.firebaseapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class my_adapter extends RecyclerView.Adapter<my_adapter.myViewholder>{
     Context context;
    ArrayList<User> userList;

    public my_adapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public my_adapter.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_row,parent,false);

        return new  myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull my_adapter.myViewholder holder, int position) {
        User model = userList.get(position);
        holder.emmail.setText(model.getEmail());
        holder.naame.setText(model.getFullName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {         //byclickingonitem on SINGLEROW(LAYOUT)we will go to message screen
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,message_screen.class);
             intent.putExtra("name",model.getFullName());
                intent.putExtra("uid",model.getId());
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void userList(ArrayList<User> searchlist) {
        userList=searchlist;
        notifyDataSetChanged();
    }

    public class myViewholder extends RecyclerView.ViewHolder {
        TextView naame,emmail;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            naame=itemView.findViewById(R.id.text_name);
            emmail=itemView.findViewById(R.id.text_email1);
        }
    }

}

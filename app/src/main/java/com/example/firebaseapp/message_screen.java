package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class message_screen extends AppCompatActivity {
    TextView username;
    Dialog dialog;
    EditText textsend;
    String receivername, receiverid, senderid;
    ImageButton btnsend;
    String receiverRoom, senderRoom;
    ArrayList<Messages> messagesArrayList;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    Intent intent;
    message_adapter message_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);
        dialog = new Dialog(message_screen.this);
        dialog.setContentView(R.layout.dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textsend = findViewById(R.id.chatsearch);
        receiverid = getIntent().getStringExtra("uid");
        receivername = getIntent().getStringExtra("name");
        username = findViewById(R.id.username);
        username.setText(" " + receivername);
        messagesArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview12);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        message_adapter = new message_adapter(message_screen.this, messagesArrayList);
        recyclerView.setAdapter(message_adapter);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        senderid = firebaseAuth.getUid();
//        receiverid=firebaseAuth.getUid();
        senderRoom = senderid + receiverid;
        receiverRoom = receiverid + senderid;
        intent = getIntent();
        DatabaseReference chatReference = database.getReference().child("Chats").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }

                message_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnsend = findViewById(R.id.btsend);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textsend.getText().toString();
                if (msg.isEmpty()) {
//                    sendmessage(fuser.getUid(), userid, msg);
                    Toast.makeText(message_screen.this, "You cannot send empty message", Toast.LENGTH_SHORT).show();
                    return;
                }
                textsend.setText(" ");
                Date date = new Date();  //for tym stamp
                Messages messages = new Messages(msg, senderid, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("Chats").child(senderRoom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("Chats").child(receiverRoom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}


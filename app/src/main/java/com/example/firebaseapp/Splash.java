package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class Splash extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser()!=null){          //for not logging out
//            Intent intent=new Intent(Splash.this,UserDashboard.class);
//            startActivity(intent);
//            finish();
//        }
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {  //The printStackTrace() method in Java is a tool used to handle exceptions and errors
                    e.getMessage();
                } finally {
//                    if (mAuth.getCurrentUser()!=null){
//                        Intent intent1=new Intent(Splash.this,UserDashboard.class);
//                        startActivity(intent1);
//                        finish();
//                    }
//                    else{
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
             //   }
                }
            }
        };
        thread.start();
    }


}

//mAuth.getCurrentUser()!=null

//if (mAuth.getCurrentUser()!=null){
//        Intent intent1=new Intent(Splash.this,UserDashboard.class);
//        startActivity(intent1);
//        }
//        else{
//        Intent intent = new Intent(Splash.this, MainActivity.class);
//        startActivity(intent);
//        finish();
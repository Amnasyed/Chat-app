package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private FirebaseAuth mAuth;
    private Button signin;
    ProgressBar progressbar;
    private EditText editemail,editpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editemail=findViewById(R.id.email_in);

        mAuth = FirebaseAuth.getInstance();            //if user doesnot want to log in again and again
        if (mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,UserDashboard.class);
            startActivity(intent);
      }
       editpass=findViewById(R.id.password_in);
       signin=findViewById(R.id.sign_in);
       signin.setOnClickListener(this);
        progressbar=findViewById(R.id.progressBar);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, signup.class));
                Log.e("Hey", "onActivityResult: ");
                break;

            case R.id.sign_in:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        String mail=editemail.getText().toString().trim();
        String password=editpass.getText().toString().trim();

        if (mail.isEmpty()) {
            editemail.setError("Email is required");
            editemail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editpass.setError("Password is required");
            editpass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            editemail.setError("Please provide valid Email");
            editemail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editpass.setError("Password is less than 6 characters");
            editpass.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
          startActivity(new Intent(MainActivity.this,UserDashboard.class));
          finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Failed to log in your account", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.INVISIBLE);
                }
            }
        });
//        For if not loging out
//if (mAuth.getCurrentUser()!=null){
//    Intent intent=new Intent(MainActivity.this,UserDashboard.class);
//    startActivity(intent);
//}



    }


}
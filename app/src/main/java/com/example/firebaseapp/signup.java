package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Button register;
    private TextView signin;
    ProgressBar bar;
    private EditText nameup, emailup, passup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameup = findViewById(R.id.name_up);
        emailup = findViewById(R.id.email_up);
        passup = findViewById(R.id.password_up);
        bar = findViewById(R.id.progressBar);
        register = findViewById(R.id.signup);
        register.setOnClickListener(this);
        signin = findViewById(R.id.sign_in);
        signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                startActivity(new Intent(this, MainActivity.class));
                Log.e("Hey", "onActivityResult: ");
                break;
            case R.id.signup:
                registerUser();
                break;
        }

    }

    public void registerUser() {


        final String name = nameup.getText().toString().trim();
        final String email = emailup.getText().toString().trim();
        String password = passup.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();
//new


        if (name.isEmpty()) {
            nameup.setError("Full name is required");
            nameup.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailup.setError("Email is required");
            emailup.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passup.setError("Password is required");
            passup.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailup.setError("Please provide valid Email");
            emailup.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passup.setError("Password is less than 6 characters");
            passup.requestFocus();
            return;
        }
        bar.setVisibility(View.VISIBLE);
        //here for auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, email, password,FirebaseAuth.getInstance().getCurrentUser().getUid());
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(signup.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                        bar.setVisibility(View.INVISIBLE);
                                    } else {
                                        Toast.makeText(signup.this, "Failed to register", Toast.LENGTH_LONG).show();
                                        bar.setVisibility(View.GONE);
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(signup.this, "Failed to register", Toast.LENGTH_LONG).show();
                            bar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}

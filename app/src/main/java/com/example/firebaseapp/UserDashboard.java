package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Notification;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    my_adapter myAdapter;         //class my_adapter
    DatabaseReference userRef;
    FirebaseUser user;
    FirebaseAuth mAuth;
    ArrayList<User> userList;
    String currentUserId;  //string made for getting currentuser
    ImageView img;
    BottomNavigationView bottomNav;
    private static NotificationManager mNotificationManager;
    EditText editsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        user = FirebaseAuth.getInstance().getCurrentUser();
       bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_acng:
                        startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_gallery:
                        startActivity(new Intent(getApplicationContext(),activity_settings.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_setting:
                        startActivity(new Intent(getApplicationContext(),activity_groups.class));
                        overridePendingTransition(0,0);
                        return true;
            }
                return false;
            }
        });
//        final Uri notificationsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid().toString();   //forcurrentuser
        editsearch = findViewById(R.id.editTextsearch);    //forEDITTEXT(SEARCH BAR)
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userList = new ArrayList<>();
        myAdapter = new my_adapter(this, userList);
        recyclerView.setAdapter(myAdapter);
        img=findViewById(R.id.log_out);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference uidRef = rootRef.child("Users").child(uid);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("fullName").getValue(String.class);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "1")
                                .setSmallIcon(R.drawable.ic_splash_logo)
                                .setContentTitle("My notification")
                                .setContentText("You signed out Successfully")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//                        mNotificationManager.notify(1, builder.build());
                        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
                        notificationManagerCompat.notify(1, builder.build());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                uidRef.addListenerForSingleValueEvent(eventListener);
                mAuth.signOut();
//                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //snapshot bcz we take snapshot from firebase
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User model = dataSnapshot.getValue(com.example.firebaseapp.User.class);


                    if (!currentUserId.equals(model.getId())) {           //for disploaying all data other than current data
                        userList.add(model);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //for FUNCTION :search
    private void filter(String text) {
        ArrayList<User> searchlist = new ArrayList<>();
        for (User user : userList) {
            if (user.getFullName().toLowerCase().contains(text.toLowerCase())) {
                searchlist.add(user);
            }
            myAdapter.userList(searchlist);
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createNotificationChannel() {

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("1", "My Notification", importance);
            channel.setDescription("You signed out Successfully");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = null;
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


        }
    }
    }
//      bottomNav.setOnNavigationItemSelectedListener(navListener);
//       if (savedInstanceState == null) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new chat_fragment()).commit();
//    }
//}
//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//  bottomNav.setOnNavigationItemSelectedListener(navListener);
//       if (savedInstanceState == null) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new chat_fragment()).commit();
//    }
//}
//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment selectedFragment = null;
//
//                    switch (item.getItemId()) {
//                        case R.id.nav_home:
//                            selectedFragment = new chat_fragment();
//                            break;
//                        case R.id.nav_setting:
//                            selectedFragment = new group_fragment();
//                            break;
////                        case R.id.nav_search:
////                            selectedFragment = new SearchFragment();
////                            break;
//                    }
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
//
//                    return true;
//                }
//
//};
//}
//}

//    @Override
//    public void onBackPressed(){
//
//    super.onBackPressed();
//    }



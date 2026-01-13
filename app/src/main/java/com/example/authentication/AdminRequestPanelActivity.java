package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AdminRequestPanelActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("students");

    private ArrayList<DataSnapshot> requestedForSignup = new ArrayList<DataSnapshot>();

    private RecyclerView recyclerView;

    private ArrayList<User> users;

    private RcAdapter adapter;

    private String firstName, lastName, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_panel);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot user: snapshot.getChildren()){

                    Boolean checkRequest = user.child("request").getValue(Boolean.class);
                    if (checkRequest == null || checkRequest == false) {
                        requestedForSignup.add(user);
                    }
                }

                // SHOWING REQ ON THE ACTIVITY
                showRequests();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdminRequestPanelActivity.this, "In listener cancel ", Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {

            int position = data.getIntExtra("position", -1);

            if (position != -1 && adapter != null) {
                adapter.removeItem(position);
            }
        }
    }


    // *** REMEMBER: REMOVE USER FROM DB IF CLICKED DECLINE REQ BTN ***
    private void showRequests(){

        recyclerView = findViewById(R.id.recyclerView);

        users = new ArrayList<User>();

        for (DataSnapshot user : requestedForSignup ){

            firstName = user.child("firstName").getValue(String.class);
            lastName = user.child("lastName").getValue(String.class);
            String name = firstName + " " + lastName;

            email = user.child("email").getValue(String.class);

            String contactNo = user.child("contactNo").getValue(String.class);
            String parentContactNo = user.child("parentContactNo").getValue(String.class);
            String dpJoinYr = user.child("dpJoinYear").getValue(String.class);

            User addUser = new User(name, email, contactNo, parentContactNo, dpJoinYr);
            users.add(addUser);

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        adapter = new RcAdapter(this, users);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

}
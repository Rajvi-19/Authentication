package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class MoreDetailActivity extends AppCompatActivity {

    private String name, email, contactNo, parentContactNo, dpJoinYr;

    private TextView setName, setEmail, setContact, setPContact, setDpYr;

    private Button moreDetailBtnAccept, moreDetailBtnReject;

    private User intent;
    private int position;

    private AdminButtonActionActivity btnCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);

        btnCall = new AdminButtonActionActivity();
        intent = (User) getIntent().getSerializableExtra("userClass");


        name = intent.getName(); // **TESTING**
        email = intent.getEmail();
        contactNo = intent.getContactNo();
        parentContactNo = intent.getParentContactNo();
        dpJoinYr = intent.getJoinYear();


        setName = findViewById(R.id.name);
        setName.setText(name);

        setEmail = findViewById(R.id.email);
        setEmail.setText(email);

        setContact = findViewById(R.id.contact_no);
        setContact.setText(contactNo);

        setPContact = findViewById(R.id.parent_contact_no);
        setPContact.setText(parentContactNo);

        setDpYr = findViewById(R.id.dp_join_yr);
        setDpYr.setText(dpJoinYr);


        position = getIntent().getIntExtra("position", -1);

        Button accept = findViewById(R.id.moreDetailaccept);
        Button reject = findViewById(R.id.moreDetailreject);

        accept.setOnClickListener(v -> {
            // CALLED BTN FROM THE ADMINBUTTONACTIVITY AND SEND THE STATE TO THE ADMIN PANEL
            btnCall.AcceptRequest(intent);
            sendResultAndFinish("accept");
        });

        reject.setOnClickListener(v -> {
            btnCall.RejectRequest(intent);
            sendResultAndFinish("reject");
        });

    }
// SEND THE STATE TO THE REQUEST PANEL OF THE ADMIN
    private void sendResultAndFinish(String action) {
        Intent result = new Intent();
        result.putExtra("action", action);
        result.putExtra("position", position);
        setResult(RESULT_OK, result);
        finish();
    }
}
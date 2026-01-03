package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MoreDetailActivity extends AppCompatActivity {

    private String name, email, contactNo, parentContactNo, dpJoinYr;

    private TextView setName, setEmail, setContact, setPContact, setDpYr;

    private Button moreDetailBtnAccept, moreDetailBtnReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);

        Intent intent = getIntent();
        name = intent.getStringExtra("detailName");
        email = intent.getStringExtra("detailEmail");
        contactNo = intent.getStringExtra("detailContactNo");
        parentContactNo = intent.getStringExtra("detailParentContactNo");
        dpJoinYr = intent.getStringExtra("detailJoinYear");


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


        moreDetailBtnAccept = findViewById(R.id.moreDetailaccept);
        moreDetailBtnReject = findViewById(R.id.moreDetailreject);

    }
}
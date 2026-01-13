package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginBtn, goToSignupBtn, goToAdmin;

    //CUSTOM TOAST
    Toast customToast;

    String name, email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        CUSTOM TOAST

        customToast = new Toast(LoginActivity.this);
        View view =getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));

        customToast.setView(view);
        customToast.setDuration(Toast.LENGTH_LONG);
        customToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        customToast.show();

//        FOR BUTTON CLICK
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        goToSignupBtn = findViewById(R.id.gotosignup_btn);
        goToSignupBtn.setOnClickListener(this);

        goToAdmin = findViewById(R.id.go_toAdmin);
        goToAdmin.setOnClickListener(this);

    }

    // FOR BTN CLICKED
    @Override
    public void onClick(View view) {

        int btnClicked = view.getId();

        if (btnClicked == R.id.gotosignup_btn){

            //  IF SIGNUP BTN IS CLICKED
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);

        } else if (btnClicked == R.id.go_toAdmin) {
            Intent intent = new Intent(this, AdminRequestPanelActivity.class);
            startActivity(intent);

        } else {

            EditText inputEmail = findViewById(R.id.login_email_input);
            EditText inputPassword = findViewById(R.id.login_password_input);

            String emailGot = inputEmail.getText().toString();
            String passwordGot = inputPassword.getText().toString();

            // IF LOGIN BTN IS CLICKED
            verifyDetails(emailGot,passwordGot);
        }

    }

    // CHECKS IF DETAILS ARE CORRECT OR NOT
    private void verifyDetails(String email, String password){

        // SEARCHING DATA THROUGH THE DATABASE
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("students");

        Query query = ref.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Email found
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String dbPassword = userSnapshot.child("password").getValue(String.class);
                        Log.d("FirebaseLogin", "Password: " + password);
                        Log.d("EnteredFirebaseLogin", "Entered Password = " + dbPassword);

                        if (dbPassword != null && dbPassword.equals(password)){

                            Log.d("FirebaseLogin", "Hellooo");

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class );
                            startActivity(intent);
                            break;
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "No Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Email not found
                    Toast.makeText(getApplicationContext(), "Email does not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
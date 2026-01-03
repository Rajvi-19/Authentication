package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener  {

    //    BlurView blurView;
    private Spinner spinner;  // for spinner


    //SIGNUP ATTRIBUTES

    EditText firstname_input, lastname_input, email_input, contact_input, parent_contact_input;
    private String signupFirstName, signupLastName, signupEmail, signupContactNo, signupParentContactNo, signupJoinYear;

    private Button signupBtn, goToLoginBtn;

    private FirebaseDatabase database;
    private DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //        FOR BLUR VIEW
//        blurView = findViewById(R.id.blurView);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            blurView.setupWith(findViewById(R.id.body_container), new RenderEffectBlur()).setBlurAutoUpdate(true).setBlurRadius(3f);
//        }
//
//        blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
//        blurView.setClipToOutline(true);


        //        FOR BATCH YEAR SELECTION
        spinner = findViewById(R.id.batchYear_input);
        ArrayList<String> joinDateOptions = addYears();

        createSpinner(joinDateOptions);



//        FOR SIGNUP BUTTON CLICK
        signupBtn = findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener(this);

        goToLoginBtn = findViewById(R.id.gotologin_btn);
        goToLoginBtn.setOnClickListener(this);


    }

    //  FOR SPINNER WIDGET
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String selectedItem = adapterView.getItemAtPosition(i).toString();
        signupJoinYear = selectedItem;
        Toast.makeText(this,selectedItem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // FOR SIGNUP BTN CLICKED
    @Override
    public void onClick(View view) {

        int btnClicked = view.getId();

        if (btnClicked == R.id.gotologin_btn){

            //  IF LOGIN BTN IS CLICKED
            redirectToLogin();

        } else {

            // IF SIGNUP BTN IS CLICKED
            if (checkDetails()) {

                // ADDING DATA TO THE DATABASE
                addDataToDatabase();

                // EVERYTHING IS OK THEN REDIRECT TO LOGIN PAGE
//                redirectToLogin();

                directTOAdmin(); // *** TEMPORARY ***

            } else {
                Toast.makeText(this, "Sign Up Failed!!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // CHECK ALL DETAILS ARE FILLED UP OR NOT
    private boolean checkDetails(){

        firstname_input = findViewById(R.id.firstname_input);
        lastname_input = findViewById(R.id.lastname_input);
        email_input = findViewById(R.id.email_input);
        contact_input = findViewById(R.id.contact_input);
        parent_contact_input = findViewById(R.id.parent_contact_input);

        signupFirstName = firstname_input.getText().toString().trim();
        signupLastName = lastname_input.getText().toString().trim();
        signupEmail = email_input.getText().toString().trim();
        signupContactNo= contact_input.getText().toString().trim();
        signupParentContactNo = parent_contact_input.getText().toString().trim();


        String[] checkStrings = {signupFirstName,signupLastName,signupEmail,signupContactNo, signupParentContactNo,signupJoinYear};

        if (checkIfEmpty()){
            return false;

        }

        for (String detail : checkStrings){
            if (detail.isEmpty())
                return false;
        }
//        FOR CHECKING EMAIL AND CONTACT

        if (!checkEmail(signupEmail) | !checkContactNo(signupContactNo) | !checkContactNo(signupParentContactNo ))
            return false;

        return true;

    }

    // REDIRECT TO LOGIN
    private void redirectToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void addDataToDatabase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("students");

//        SENDING REQUEST WILL BE BY-DEFAULT FALSE AS REQUEST IS SENT FIRST TO ADMIN AND WILL BE TRUE WHEN WILL ACCEPT IT
        HelperClass details = new HelperClass(signupFirstName,signupLastName,signupEmail,signupContactNo,signupParentContactNo, signupJoinYear, false);
        reference.child(signupFirstName+" "+signupLastName).setValue(details);

    }

    private Boolean checkEmail(String  email){

//       ***CAPITAL LETTERS LOGIC LEFT***

        if (!email.contains("@") | !email.contains("."))
            return false;

        return true;
    }

    private Boolean checkContactNo(String contactNo){
        if (contactNo.length() < 10 | contactNo.length() > 10 )
            return false;

        return true;
    }

    private Boolean checkIfEmpty( ){

        if (firstname_input == null) {
            Toast.makeText(this, "firstname_input is missing from layout!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (lastname_input == null) {
            Toast.makeText(this, "lastname_input is missing from layout!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (email_input == null) {
            Toast.makeText(this, "email_input is missing from layout!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (contact_input == null) {
            Toast.makeText(this, "contact_input is missing from layout!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (parent_contact_input == null) {
            Toast.makeText(this, "parent_contact_input is missing from layout!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (spinner == null) {

            Toast.makeText(this, "semester_input (spinner) is missing from layout!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

//    FOR CREATING A SPINNER
    private void createSpinner(ArrayList<String> arrayList){

        ArrayAdapter<String> adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                arrayList
        );
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

    }

//    FOR SHOWING YEARS

    private ArrayList<String> addYears(){

        ArrayList<String> arrayList = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for (int i=2; i>=0; i--){

            int year = currentYear-i;
            arrayList.add(String.valueOf(year));

        }

        return arrayList;

    }

    // *** TEMPORARY ***
    private void directTOAdmin(){
        Intent intent = new Intent(this, AdminRequestPanelActivity.class); // ** TEMPORARY  CHANGE IT TO THE ADMIN**
        startActivity(intent);
    }


}
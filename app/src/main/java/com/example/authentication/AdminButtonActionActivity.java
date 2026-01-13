package com.example.authentication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AdminButtonActionActivity {
    String name, email;

    DatabaseReference reference;

    protected void AcceptRequest(User user){

        name = user.getName();
        email = user.getEmail();

         reference = FirebaseDatabase.getInstance()
                .getReference("students")
                .child(name);

        //  SENDING DATA TO FORWARD THE MAIL
        boolean response = sendData(name, email);
        Log.d("Response", String.valueOf(response));
        if (response){ //response
            addPasswordField();
            stateChangeRequestField();
        }
        else{
            Log.d("Response", "falseeeeee");
        }
    }

    protected void RejectRequest(User userToRemove){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child(userToRemove.getName());
        reference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Log.d("RejectRequest", "Completely Removed!!");
            else
                Log.d("RejectRequest", "Wasn't able to remove!!");
        });

    }

    // CHANGING THE STATE OF THE THE REQUEST FIELD

    private void stateChangeRequestField(){

        final Boolean[] getValueOfReq = new Boolean[1];

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    getValueOfReq[0] = snapshot.child("request").getValue(Boolean.class);

                    if (!getValueOfReq[0]){
                        reference.child("request").setValue(true);
                        Log.d("Request", "Turned To True");
                    }
                    else{
                        Log.d("Request","Turned False");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("AdminButtonActionActivity", "onCancelled: AdminButtonActionActivity has a proble in request field");
            }
        });

    }


    // FOR ADDING PASSWORD TO THE FIREBASE STUDENT FIELD
    private void addPasswordField() {
        getValuesFromSheet(password -> {
            Log.d("Password", password);

            if (!password.isEmpty()) {
                HelperClass setPassword = new HelperClass(password);
                reference.child("password").setValue(setPassword.getPassword());
            } else {
                Log.d("Firebase", "Password is empty, not adding!");
            }
        });
    }

    // Step 1: Define a callback interface
    interface SheetCallback {
        void onPasswordFetched(String password);
    }

    // Step 2: Update your method to use callback
    private String getValuesFromSheet(SheetCallback callback) {

        final String[] sendPassword = new String[1];
        new Thread(() -> {
            try {
                StringBuilder password = new StringBuilder("");

                String spreadsheetId = "1QJouksp4rXmfeQam-2awleDlbdhWuG2lm9UxJepmBjc";
                String range = "Sheet1";
                String apiKey = "AIzaSyDBCkdU2utdWC1n3TKVNuqzlt_3oogod7Y";

                String url = "https://sheets.googleapis.com/v4/spreadsheets/"
                        + spreadsheetId + "/values/" + range + "?key=" + apiKey;

                URL sheetUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) sheetUrl.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder jsonResult = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonResult.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(jsonResult.toString());
                JSONArray values = jsonObject.getJSONArray("values");

                String targetName = name;
                String targetEmail = email;

                JSONArray foundRow = null;

                for (int i = 1; i < values.length(); i++) {
                    JSONArray row = values.getJSONArray(i);

                    String rowName = row.length() > 0 ? row.getString(0) : "";
                    String rowEmail = row.length() > 1 ? row.getString(1) : "";

                    if (rowName.equalsIgnoreCase(targetName) && rowEmail.equalsIgnoreCase(targetEmail)) {
                        foundRow = row;
                        break;
                    }
                }

                if (foundRow != null) {
                    password.append(foundRow.length() > 2 ? foundRow.getString(2) : "");
                }

                // Step 3: Pass result back to main thread
                String finalPassword = password.toString();
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onPasswordFetched(finalPassword);
                });
                sendPassword[0] = finalPassword;

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onPasswordFetched("");
                });
            }
        }).start();
        return sendPassword[0];
    }


    // SENDING DATA TO WEBHOOKS MAKE.COM
    private boolean sendData(String name, String email) {
        String sendName = name;
        String sendEmail = email;

        // ALLOW NETWORK ON MAIN THREAD (for quick test only!)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL("https://hook.eu2.make.com/md2370e4y8mtop9q2o7xaca7bxg2ber1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setDoOutput(true);

            // Form-data style body
            String urlParameters = "name=" + URLEncoder.encode(sendName, "UTF-8")
                    + "&email=" + URLEncoder.encode(sendEmail, "UTF-8");

            // Write body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlParameters.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            Log.d("Response Code", String.valueOf(code));

            InputStream responseStream = (code >= 200 && code < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(responseStream));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.d("Response", response.toString());

            if (response.toString().equals("Row Updated"))
                return true;

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}



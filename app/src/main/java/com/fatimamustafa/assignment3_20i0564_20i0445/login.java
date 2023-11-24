package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText email;
    EditText password;
    private TextView loginStatusTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password=findViewById(R.id.password);

        loginStatusTextView = findViewById(R.id.loginStatusTextView);

        Button loginTextView = findViewById(R.id.Login_butt_screen1);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser();
            }
        });
        // Find the TextView
        TextView textView = findViewById(R.id.frgt_btn_sc1);
        // Set an OnClickListener for the TextView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to screen2
                Intent intent = new Intent(login.this, forgotpass.class);
                // Start the new activity
                startActivity(intent);
            }
        });
        // Find the TextView for "Sign Up"
        TextView signUpTextView = findViewById(R.id.signup_btn_sc1);
        // Set an OnClickListener for the "Sign Up" TextView
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to screen2
                Intent intent = new Intent(login.this, signup.class);
                // Start the new activity
                startActivity(intent);
            }
        });
    }
    private void getUser() {
        String url = "http://192.168.10.51/assign3smd/getuser.php";
        String enteredEmail = email.getText().toString().trim();
        String enteredPassword = password.getText().toString().trim();

        GetUserAsyncTask getUserAsyncTask = new GetUserAsyncTask();
        getUserAsyncTask.execute(url, enteredEmail, enteredPassword);
    }

    public class GetUserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                // Create the URL
                URL url = new URL(params[0]);

                // Open a connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                connection.setRequestMethod("POST");

                // Set the timeout for both the connection and the read
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                // Enable input/output streams and set input as true because you are sending data
                connection.setDoInput(true);
                connection.setDoOutput(true);

                // Add your data to the request
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");

                // Construct the POST data using input values
                String postData = "email=" + URLEncoder.encode(params[1], "UTF-8") +
                        "&password=" + URLEncoder.encode(params[2], "UTF-8");

                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                // Connect to the server
                connection.connect();

                // Read the response
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(reader);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                br.close();
                reader.close();
                connection.disconnect();

                // Log and store the response
                Log.d("response", builder.toString());
                response = builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Handle the response as needed
            // You might want to update the UI or perform other actions based on the server response
            Log.d("response", "PostExecute: " + s);
            if (s != null && !s.isEmpty()) {
                try {
                    // Parse the response as a JSON object
                    JSONObject jsonResponse = new JSONObject(s);

                    // Check if the response contains the "Status" key
                    if (jsonResponse.has("Status")) {
                        // Retrieve the value of the "Status" key
                        int status = jsonResponse.getInt("Status");

                        if (status == 1) {
                            // The response indicates success
                            Toast.makeText(login.this, "User Found", Toast.LENGTH_SHORT).show();
                            loginStatusTextView.setText("Login Successful");

                            // Access the "User" field and then get the "id" field
                            JSONObject userObject = jsonResponse.getJSONObject("User");
                            int userId = userObject.getInt("id");

                            SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("userId", userId);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            Intent intent = new Intent(login.this, Home.class);
                            startActivity(intent);
                        } else {
                            // The response indicates failure
                            Toast.makeText(login.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            loginStatusTextView.setText("Login Failed");
                        }
                    } else {
                        // Handle the case where the "Status" key is not present in the response
                        Toast.makeText(login.this, "Invalid Response Format", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // Handle JSON parsing error
                    Log.d("error",e.toString());
                    Toast.makeText(login.this, "JSON Parsing Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                // Handle the case where the response is empty or null
                Toast.makeText(login.this, "Empty or Null Response", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
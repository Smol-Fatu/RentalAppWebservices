package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText name, phno, email,password;
    TextView login;
    Button signup;
    private String selectedCountry;
    private String selectedCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = findViewById(R.id.name);
        phno = findViewById(R.id.phoneno);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup_btn_sc3);
        login = findViewById(R.id.login_btn_sc3);

        Spinner countrySpinner = findViewById(R.id.spinner_country);
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.country_array,  // Replace with your array resource or list
                android.R.layout.simple_spinner_item
        );

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                selectedCountry = parentView.getItemAtPosition(position).toString();
                // Do something with the selected item
                Log.d("Selected Country", selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (optional)
            }
        });

        Spinner citySpinner = findViewById(R.id.spinner_city);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.city_array,  // Replace with your array resource or list
                android.R.layout.simple_spinner_item
        );

        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                selectedCity = parentView.getItemAtPosition(position).toString();
                // Do something with the selected item
                Log.d("Selected City", selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (optional)
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to send a POST request
                addUser();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(Add.this,MainActivity.class);
//                startActivity(i);
            }
        });

    }

    private void addUser() {
        String url = "http://172.16.48.81/assign3smd/insert.php";

        // Retrieve input values
        final String newName = name.getText().toString().trim();
        final String newPhno = phno.getText().toString().trim();
        final String newEmail = email.getText().toString().trim();
        final String newPassword = password.getText().toString().trim();

        // Check for empty fields
        if (newName.isEmpty() || newPhno.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty() || selectedCountry.isEmpty() || selectedCity.isEmpty()) {
            Toast.makeText(signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a StringRequest for the POST request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response after adding the contact
                        Toast.makeText(signup.this, response, Toast.LENGTH_SHORT).show();
                        // You may want to navigate back to the main activity or handle the response accordingly
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(signup.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add parameters to the request body
                Map<String, String> params = new HashMap<>();
                params.put("name", newName);
                params.put("phone", newPhno);
                params.put("email", newEmail);
                params.put("password", newPassword);
                params.put("country", selectedCountry);
                params.put("city", selectedCity);
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        PostData postData = new PostData(newName, newPhno, newEmail, newPassword, selectedCountry, selectedCity);
        postData.execute();


    }
    public class PostData extends AsyncTask<Void, Void, String> {
        String res;
        String newName, newPhno, newEmail, newPassword, newCountry, newCity;

        // Constructor to receive input values
        public PostData(String newName, String newPhno, String newEmail, String newPassword, String newCountry, String newCity) {
            this.newName = newName;
            this.newPhno = newPhno;
            this.newEmail = newEmail;
            this.newPassword = newPassword;
            this.newCountry = newCountry;
            this.newCity = newCity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Create the URL
                URL url = new URL("http://192.168.10.12/assign3smd/insert.php");

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
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                // Construct the POST data using input values
                String postData = "name=" + URLEncoder.encode(newName, "UTF-8") +
                        "&phone=" + URLEncoder.encode(newPhno, "UTF-8") +
                        "&email=" + URLEncoder.encode(newEmail, "UTF-8") +
                        "&password=" + URLEncoder.encode(newPassword, "UTF-8") +
                        "&country=" + URLEncoder.encode(newCountry, "UTF-8") +
                        "&city=" + URLEncoder.encode(newCity, "UTF-8");

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
                res = builder.toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Handle the response as needed
            // You might want to update the UI or perform other actions based on the server response
            Toast.makeText(signup.this, "PostExecute", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView name;
    RecyclerView recyclerView1,recyclerView2;
    MyAdapter myAdapter1,myAdapter2;
    ArrayList<Items> list1,list2;
    private DatabaseHelper dbHelper;
    ImageView imageView;
    private RequestQueue requestQueue;
    private static final String ONESIGNAL_APP_ID = "90ca0b6f-3abd-4c19-afbe-82b1d0afc2b9";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        requestQueue = Volley.newRequestQueue(this);
        name = findViewById(R.id.name);
        dbHelper = new DatabaseHelper(this);
        recyclerView1 = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerViewRecent);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        myAdapter1 = new MyAdapter(list1, this);
        myAdapter2 = new MyAdapter(list2, this);

        // Set adapters for RecyclerViews
        recyclerView1.setAdapter(myAdapter1);
        recyclerView2.setAdapter(myAdapter2);

        fetchItemsFromSQLite();
        getUserDetails(0);

        imageView= findViewById(R.id.edit_btn_sc10);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this,EditProfile.class);
            startActivity(intent);
            finish();
        });
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
            if (r.isSuccess()) {
                if (r.getData()) {
                    // `requestPermission` completed successfully and the user has accepted permission
                }
                else {
                    // `requestPermission` completed successfully but the user has rejected permission
                }
            }
            else {
                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
            }
        }));

        Button notificationButton = findViewById(R.id.notification);
        notificationButton.setOnClickListener(v -> {

                    String id = OneSignal.getUser().getPushSubscription().getId();
                    Intent intent = new Intent(Profile.this, Home.class);
                    intent.putExtra("message", "This is a notification");
                    startActivity(intent);
                });

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.homeItem);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.homeItem) {
                    Intent intent = new Intent(Profile.this,Home.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.searchItem) {
                    Intent intent = new Intent(Profile.this,Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.addItem) {
                    Intent intent = new Intent(Profile.this,Additem.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.chatItem) {
                    Intent intent = new Intent(Profile.this,Chat.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.profileItem) {
                    Intent intent = new Intent(Profile.this,Profile.class);
                    startActivity(intent);
                    finish();
                    return true;

                }
                return false;
            }
        });

    }
    private void getUserDetails(int userId) {
        String url = "http://192.168.10.51/assign3smd/get_user.php";

        // Use Volley or another networking library to make the request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("Status");
                            if (status == 1) {
                                String name = jsonResponse.getString("Name");
                                String profilePic = jsonResponse.getString("ProfilePic");

                                String imageUrl = profilePic;
                                ImageView itemImage = findViewById(R.id.profilepic);
                                Picasso.get()
                                        .load("http://192.168.10.51/assign3smd/"+imageUrl)
                                        .error(R.drawable.baseline_account_circle_24) // Replace with your error placeholder
                                        .into(itemImage);
                            } else {
                                String message = jsonResponse.getString("Message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON Exception", e.toString());
                            Toast.makeText(getApplicationContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error message for debugging
                        Log.e("Volley Error", "Error retrieving user details: " + error.getMessage());

                        // Show a toast with a generic error message
                        Toast.makeText(getApplicationContext(), "Error retrieving user details", Toast.LENGTH_SHORT).show();
                    }
            }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        // Add the request to the RequestQueue (assuming you have a RequestQueue instance)
        requestQueue.add(stringRequest);
    }
    private void fetchItemsFromSQLite() {
        // Assuming you have a method in DatabaseHelper to get all items
        ArrayList<Items> itemsList = dbHelper.getAllItems();

        // Check if the list is not empty
        if (!itemsList.isEmpty()) {
            list1.clear();
            list2.clear();

            for (Items item : itemsList) {
                // Assuming you want to display all items in the same way
                list1.add(item);
                list2.add(item);
                int itemId = dbHelper.getItemIdByName(item.getItemname());
                if (itemId != -1) {
                    // Item ID found, use it as needed
                    //Toast.makeText(this, "Item ID: " + itemId, Toast.LENGTH_SHORT).show();
                } else {
                    // Item not found
                    Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                Log.d("image",item.getImageUrl());
            }

            // Notify the adapter that the data set has changed
            myAdapter1.notifyDataSetChanged();
            myAdapter2.notifyDataSetChanged();
        } else {
            Toast.makeText(Profile.this, "No items found in SQLite database", Toast.LENGTH_SHORT).show();
        }
    }
}
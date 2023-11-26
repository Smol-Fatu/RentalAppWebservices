package com.fatimamustafa.assignment3_20i0564_20i0445;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.ArrayList;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView name;
    RecyclerView recyclerView1,recyclerView2,recyclerView3;
    MyAdapter myAdapter1,myAdapter2,myAdapter3;
    ArrayList<Items> list1,list2,list3;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        dbHelper = new DatabaseHelper(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.homeItem);

        recyclerView1 = findViewById(R.id.recyclerViewFeaturedItems);
        recyclerView2 = findViewById(R.id.recyclerViewRecentItems);
        recyclerView3 = findViewById(R.id.recyclerViewYourItems);

        recyclerView1.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView3.setHasFixedSize(true);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        myAdapter1 = new MyAdapter(list1, this);
        myAdapter2 = new MyAdapter(list2, this);
        myAdapter3 = new MyAdapter(list3, this);

        //dbHelper.deleteAllData();
        //fetchItems();
        fetchItemsFromSQLite();

        recyclerView1.setAdapter(myAdapter1);
        recyclerView2.setAdapter(myAdapter2);
        recyclerView3.setAdapter(myAdapter3);

        SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("userId", -1);


        if (userId != -1) {
            Toast.makeText(Home.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Home.this, "User ID not found", Toast.LENGTH_SHORT).show();
        }


        name=findViewById(R.id.name);
        name.setText("Fatima");

        // Assuming you are in Activity B
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("message")) {
            String message = intent.getStringExtra("message");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.homeItem);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.homeItem) {
                    Intent intent = new Intent(Home.this,Home.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.searchItem) {
                    Intent intent = new Intent(Home.this,Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.addItem) {
                    Intent intent = new Intent(Home.this,Additem.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.chatItem) {
                    Intent intent = new Intent(Home.this,Chat.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.profileItem) {
                    Intent intent = new Intent(Home.this,Profile.class);
                    startActivity(intent);
                    finish();
                    return true;

                }
                return false;
            }
        });
        // Find the "Logout" button by its ID
        Button logoutButton = findViewById(R.id.Logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign the user out

                SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // Example: Navigate to the login or splash screen after logout
                Intent intent = new Intent(Home.this, login.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back.
            }
        });

    }
    private void fetchItems() {
        String url = "http://192.168.10.51/assign3smd/getitems.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parse the JSON response and add items to the list
                        list1.clear();
                        list2.clear();
                        list3.clear();
                        int responseLength = response.length();
                        Toast.makeText(Home.this, "Response Length: " + responseLength, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < responseLength; i++) {
                            try {
                                JSONObject itemJson = response.getJSONObject(i);
                                Items item = new Items(
                                        itemJson.getString("item_name"),
                                        itemJson.getString("item_desc"),
                                        itemJson.getString("item_rate"),
                                        itemJson.getString("item_city"),
                                        "11-20-2023",
                                        itemJson.getString("item_img_path")
                                );
                                long result = dbHelper.addRoomRentalItem(new Items(
                                        itemJson.getString("item_name"),
                                        itemJson.getString("item_desc"),
                                        itemJson.getString("item_rate"),
                                        itemJson.getString("item_city"),
                                        "11-20-2023",
                                        itemJson.getString("item_img_path")
                                ));

                                if (result != -1) {
                                    Toast.makeText(Home.this, "Item added to the database.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Home.this, "Error adding item to the database.", Toast.LENGTH_LONG).show();
                                }
                                String itemName = itemJson.getString("item_name");
                                String itemDesc = itemJson.getString("item_desc");
                                String message = "Item Name: " + itemName + ", Description: " + itemDesc;

                                // Display the message in a Toast
                                //Toast.makeText(Home.this, message, Toast.LENGTH_SHORT).show();
                                list1.add(item);
                                list2.add(item);
                                list3.add(item);
                            } catch (JSONException e) {
                                Toast.makeText(Home.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        // Notify the adapter that the data set has changed
                        myAdapter1.notifyDataSetChanged();
                        myAdapter2.notifyDataSetChanged();
                        myAdapter3.notifyDataSetChanged();
                        //Toast.makeText(Home.this, "added", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(Home.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void fetchItemsFromSQLite() {
        // Assuming you have a method in DatabaseHelper to get all items
        ArrayList<Items> itemsList = dbHelper.getAllItems();

        // Check if the list is not empty
        if (!itemsList.isEmpty()) {
            list1.clear();
            list2.clear();
            list3.clear();

            for (Items item : itemsList) {
                // Assuming you want to display all items in the same way
                list1.add(item);
                list2.add(item);
                list3.add(item);
                int itemId = dbHelper.getItemIdByName(item.getItemname());
                if (itemId != -1) {
                    // Item ID found, use it as needed
                    //Toast.makeText(this, "Item ID: " + itemId, Toast.LENGTH_SHORT).show();
                } else {
                    // Item not found
                    Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                //Log.d("image",item.getImageUrl());
            }

            // Notify the adapter that the data set has changed
            myAdapter1.notifyDataSetChanged();
            myAdapter2.notifyDataSetChanged();
            myAdapter3.notifyDataSetChanged();
        } else {
            Toast.makeText(Home.this, "No items found in SQLite database", Toast.LENGTH_SHORT).show();
        }
    }

}
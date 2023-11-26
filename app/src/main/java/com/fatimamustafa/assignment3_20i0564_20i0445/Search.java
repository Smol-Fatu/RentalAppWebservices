package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private DatabaseHelper dbHelper;
    private static final String TABLE_ROOM_RENTAL = "room_rental";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_ITEM_DESC = "item_desc";
    private static final String COLUMN_ITEM_RATE = "item_rate";
    private static final String COLUMN_ITEM_CITY = "item_city";
    private static final String COLUMN_ITEM_IMG_PATH = "item_img_path";
    ArrayList<Items> searchResults = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DatabaseHelper(this);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(searchResults, this);
        recyclerView.setAdapter(adapter);
        MyAdapter.OnItemClickListener itemClickListener = new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String itemData) {
                // Handle item click here
                // You can obtain the position and item data
                // ...

                // If you want to open a new activity with the details, you can do something like this:
                Intent intent = new Intent(Search.this, ItemDetails.class);
                intent.putExtra("item_data", itemData);
                startActivity(intent);
            }
        };

        adapter.setOnItemClickListener(itemClickListener);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query changes
                searchResults=dbHelper.searchItems(newText);
                adapter.updateData(searchResults);

                return true;
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.homeItem);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.homeItem) {
                    Intent intent = new Intent(Search.this,Home.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.searchItem) {
                    Intent intent = new Intent(Search.this,Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.addItem) {
                    Intent intent = new Intent(Search.this,Additem.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.chatItem) {
                    Intent intent = new Intent(Search.this,Chat.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.profileItem) {
                    Intent intent = new Intent(Search.this,Profile.class);
                    startActivity(intent);
                    finish();
                    return true;

                }
                return false;
            }
        });
    }
}
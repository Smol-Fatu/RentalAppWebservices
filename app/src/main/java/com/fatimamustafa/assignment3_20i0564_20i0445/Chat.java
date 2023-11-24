package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Chat extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.homeItem);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.homeItem) {
                    Intent intent = new Intent(Chat.this,Home.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.searchItem) {
                    Intent intent = new Intent(Chat.this,Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id==R.id.addItem) {
                    Intent intent = new Intent(Chat.this,Additem.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.chatItem) {
                    Intent intent = new Intent(Chat.this,Chat.class);
                    startActivity(intent);
                    finish();
                    return true;

                } else if (id==R.id.profileItem) {
                    Intent intent = new Intent(Chat.this,Profile.class);
                    startActivity(intent);
                    finish();
                    return true;

                }
                return false;
            }
        });
    }
}
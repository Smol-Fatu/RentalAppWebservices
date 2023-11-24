package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // In your SplashScreenActivity
        SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user is already logged in
                if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                    // User is logged in, navigate to home screen
                    Intent intent = new Intent(splash.this, Home.class);
                    // Start the new activity
                    startActivity(intent);
                    finish();  // Finish the splash screen activity
                } else {
                    // User is not logged in, proceed with login or show login screen
                    Intent intent = new Intent(splash.this, login.class);
                    // Start the new activity
                    startActivity(intent);
                }
            }
        }, 5000); // 5000 milliseconds (5 seconds)
    }
}
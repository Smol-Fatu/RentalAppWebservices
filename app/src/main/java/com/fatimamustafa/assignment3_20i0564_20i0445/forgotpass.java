package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class forgotpass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        // Find the TextView
        TextView textView = findViewById(R.id.go_back_btn_scr2);
        // Set an OnClickListener for the TextView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to screen2
                Intent intent = new Intent(forgotpass.this, emailverif.class);
                // Start the new activity
                startActivity(intent);
            }
        });
    }
}
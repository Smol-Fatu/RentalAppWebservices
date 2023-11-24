package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class emailverif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverif);
        ImageView closeView = findViewById(R.id.back_btn_sc4);

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the new activity
                Intent intent = new Intent(emailverif.this, login.class);

                // Start the new activity
                startActivity(intent);
            }
        });
    }
}
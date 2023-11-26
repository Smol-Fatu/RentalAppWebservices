package com.fatimamustafa.assignment3_20i0564_20i0445;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ItemDetails extends AppCompatActivity {
    TextView itemNameTextView, priceTextView, locationTextView, dateTextView, descriptionTextView;
    View view;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("item_data")) {
            String itemData = intent.getStringExtra("item_data");
            itemNameTextView = findViewById(R.id.name);
            priceTextView = findViewById(R.id.rate);
            locationTextView = findViewById(R.id.location);
            dateTextView = findViewById(R.id.date);
            descriptionTextView = findViewById(R.id.description);
            view = findViewById(R.id.back);
            Toast.makeText(this, itemData, Toast.LENGTH_SHORT).show();
            Log.d("ItemDetails", itemData);
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Items item = dbHelper.getItemByName(itemData);
            String imageUrl = item.getImageUrl();
            ImageView itemImage = findViewById(R.id.item_image);
            Picasso.get()
                    .load("http://192.168.10.51/assign3smd/" + imageUrl)
                    .error(R.drawable.spotit) // Replace with your error placeholder
                    .into(itemImage);
            itemNameTextView.setText(item.getItemname());
            priceTextView.setText(item.getPrice());
            locationTextView.setText(item.getLocation());
            dateTextView.setText(item.getDate());
            descriptionTextView.setText(item.getDescription());
        }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ItemDetails.this, Search.class);
                    startActivity(intent);
                }
            });
        EditText editTextReview = findViewById(R.id.edittextreview);
        Button buttonAddReview = findViewById(R.id.buttonAddReview);
        Button buttonUpload = findViewById(R.id.buttonUpload);
        TextView textViewReviews = findViewById(R.id.reviews);
        buttonAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the EditText and Upload button
                editTextReview.setVisibility(View.VISIBLE);
                buttonUpload.setVisibility(View.VISIBLE);
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewReviews.setText(editTextReview.getText().toString());
                // Hide the EditText and Upload button
                editTextReview.setVisibility(View.GONE);
                buttonUpload.setVisibility(View.GONE);

            }
        });
    }

}
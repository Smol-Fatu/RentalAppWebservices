package com.fatimamustafa.assignment3_20i0564_20i0445;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import android.util.Base64;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Items> items;
    private Context context;

    public MyAdapter(ArrayList<Items> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Items item = items.get(position);
        String imageUrl = item.getImageUrl();
        ImageView itemImage = holder.itemView.findViewById(R.id.item_image);
        Picasso.get()
                .load("http://192.168.10.51/assign3smd/"+imageUrl)
                .error(R.drawable.spotit) // Replace with your error placeholder
                .into(itemImage);
        holder.itemNameTextView.setText(item.getItemname());
        holder.priceTextView.setText(item.getPrice());
        holder.locationTextView.setText(item.getLocation());
        holder.dateTextView.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView priceTextView;
        TextView locationTextView;
        TextView dateTextView;
        ImageView itemImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemname);
            priceTextView = itemView.findViewById(R.id.price);
            locationTextView = itemView.findViewById(R.id.location);
            dateTextView = itemView.findViewById(R.id.date);
            itemImageView = itemView.findViewById(R.id.item_image);
        }
    }
    public void updateData(ArrayList<Items> newData) {
        items.clear();
        items.addAll(newData);
        notifyDataSetChanged();
    }
}



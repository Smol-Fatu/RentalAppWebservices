package com.fatimamustafa.assignment3_20i0564_20i0445;
public class Items {
    String itemname;
    String price;
    String location;
    String date;
    String imageUrl;

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Items() {
        this.description = "Default description";
        this.itemname = "Default Item Name";
        this.price = "$0.00";
        this.location = "Default Location";
        this.date = "Default Date";
        this.imageUrl = "default_image_url"; // You can set a default image URL or null as needed
    }
    public Items(String itemname, String description, String price, String location, String date, String imageUrl) {
        this.itemname = itemname;
        this.price = price;
        this.location = location;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

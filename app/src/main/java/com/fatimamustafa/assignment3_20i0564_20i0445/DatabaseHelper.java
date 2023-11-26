package com.fatimamustafa.assignment3_20i0564_20i0445;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "room_rental.db";
    private static final int DATABASE_VERSION = 1;

    // Define table and columns
    private static final String TABLE_ROOM_RENTAL = "room_rental";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_ITEM_DESC = "item_desc";
    private static final String COLUMN_ITEM_RATE = "item_rate";
    private static final String COLUMN_ITEM_CITY = "item_city";
    private static final String COLUMN_ITEM_IMG_PATH = "item_img_path";

    private static final String CREATE_TABLE_ROOM_RENTAL =
            "CREATE TABLE " + TABLE_ROOM_RENTAL + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ITEM_NAME + " TEXT," +
                    COLUMN_ITEM_DESC + " TEXT," +
                    COLUMN_ITEM_RATE + " TEXT," +
                    COLUMN_ITEM_CITY + " TEXT," +
                    COLUMN_ITEM_IMG_PATH + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ROOM_RENTAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }

    // Database operations (CRUD) methods go here...

    // Add a new room rental item
    public long addRoomRentalItem(Items item) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ITEM_NAME, item.getItemname());
            values.put(COLUMN_ITEM_DESC, item.getDescription());
            values.put(COLUMN_ITEM_RATE, item.getPrice());
            values.put(COLUMN_ITEM_CITY, item.getLocation());
            values.put(COLUMN_ITEM_IMG_PATH, item.getImageUrl());

            result = db.insert(TABLE_ROOM_RENTAL, null, values);
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error inserting room rental item: " + e.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    // Get all room rental items
    public ArrayList<Items> getAllItems() {
        ArrayList<Items> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_ID,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_DESC,
                COLUMN_ITEM_RATE,
                COLUMN_ITEM_CITY,
                COLUMN_ITEM_IMG_PATH
        };

        Cursor cursor = db.query(
                TABLE_ROOM_RENTAL,  // The table to query
                projection,  // The columns to return
                null,   // The columns for the WHERE clause
                null,   // The values for the WHERE clause
                null,   // don't group the rows
                null,   // don't filter by row groups
                null    // The sort order
        );

        while (cursor.moveToNext()) {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_DESC));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_RATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_CITY));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_IMG_PATH));

            // Create an Items object and add it to the list
            Items item = new Items(itemName, description, price, location, "11-23-2023", imageUrl);
            itemList.add(item);
        }

        cursor.close();
        return itemList;
    }
    public void deleteDataById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) };

        // Delete the row(s) based on the condition
        db.delete(TABLE_ROOM_RENTAL, whereClause, whereArgs);

        // Close the database
        db.close();
    }

    @SuppressLint("Range")
    public int getItemIdByName(String itemName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int itemId = -1; // Default value if not found

        Cursor cursor = db.query(
                TABLE_ROOM_RENTAL, // Replace with your actual table name
                new String[]{"id"},
                "item_name = ?",  // Assuming "itemname" is the column containing item names
                new String[]{itemName},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            itemId = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
        }

        return itemId;
    }
    public void updateimageurl(String newurl, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        // Set the new value for the "item_img_path" column
        ContentValues values = new ContentValues();
        values.put("item_img_path", newurl);

        // Define the WHERE clause
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        // Update the row based on the condition
        db.update(TABLE_ROOM_RENTAL, values, whereClause, whereArgs);

        // Close the database
        db.close();
    }

    public ArrayList<Items> searchItems(String query) {
        ArrayList<Items> searchResults = new ArrayList<>();
        Log.d("SearchActivity", "Searching for: " + query);


        searchResults.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                COLUMN_ID,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_DESC,
                COLUMN_ITEM_RATE,
                COLUMN_ITEM_CITY,
                COLUMN_ITEM_IMG_PATH
        };

        // Define the WHERE clause based on the name column
        String selection = COLUMN_ITEM_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};

        // Query the database
        Cursor cursor = ((SQLiteDatabase) db).query(
                TABLE_ROOM_RENTAL,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_DESC));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_RATE));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_CITY));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_IMG_PATH));

                Items item = new Items(itemName, description, price, location, "11-23-2023", imageUrl);
                searchResults.add(item);
            } while (cursor.moveToNext());

            // Close the cursor after extracting data
            cursor.close();
        }
        return searchResults;
    }
    @SuppressLint("Range")
    public Items getItemByName(String itemName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Items item = null;

        String query = "SELECT * FROM " + TABLE_ROOM_RENTAL +
                " WHERE " + COLUMN_ITEM_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{itemName});

        if (cursor.moveToFirst()) {
            // Assuming your Items class has a constructor that takes all columns as parameters
            item = new Items(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESC)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_RATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CITY)),
                    "11-23-2023",
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_IMG_PATH))
            );
        }

        cursor.close();
        db.close();

        return item;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ROOM_RENTAL, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'your_table_name'");
        db.close();
    }


    // ... (add update and delete methods if needed)
}

package com.fatimamustafa.assignment3_20i0564_20i0445;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    ImageView uploadphoto;
    TextView updateProfile;
    EditText userName, phone, country, city;
    private String selectedCity;
    private String selectedCountry;
    int DP_REQUEST_CODE = 200;
    TextView UploadImageOnServerButton;
    ImageView GetImageFromGalleryButton;
    ImageView ShowSelectedImage;

    Bitmap FixBitmap;

    String ImageTag = "profile_pic";
    String ImageName = "image_data";
    String UserNameTag = "name";
    String PhoneTag = "phone";
    String CountryTag = "country";
    String CityTag = "city";

    ProgressDialog progressDialog;

    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    String GetImageNameFromEditText;
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    boolean check = true;

    private DatabaseHelper dbHelper;
    int userId;
    ImageView img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dbHelper = new DatabaseHelper(this);

        uploadphoto = findViewById(R.id.uploadphoto);
        userName = findViewById(R.id.userName);
        phone = findViewById(R.id.phone);
        updateProfile = findViewById(R.id.savechanges);


        SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", MODE_PRIVATE);

        userId = sharedPreferences.getInt("userId", -1);

        Spinner citySpinner = findViewById(R.id.spinner_city);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.city_array,
                android.R.layout.simple_spinner_item
        );



        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCity = parentView.getItemAtPosition(position).toString();
                // Log.d("Selected City", selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Spinner countrySpinner = findViewById(R.id.spinner_country);
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.country_array,  // Replace with your array resource or list
                android.R.layout.simple_spinner_item
        );

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                selectedCountry = parentView.getItemAtPosition(position).toString();
                // Do something with the selected item
                Log.d("Selected Country", selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (optional)
            }
        });
        GetImageFromGalleryButton = findViewById(R.id.uploadphoto);
        UploadImageOnServerButton = findViewById(R.id.savechanges);
        ShowSelectedImage = findViewById(R.id.uploadedimage);

        byteArrayOutputStream = new ByteArrayOutputStream();

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImageNameFromEditText = userName.getText().toString();
                UploadImageToServer();
            }
        });


        if (ContextCompat.checkSelfPermission(EditProfile.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }

        // Assuming you want to go back to the home activity after updating the profile
        ImageView cross = findViewById(R.id.back_btn_sc13);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, Profile.class);
                // Start the new activity
                startActivity(intent);
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Photo Gallery", "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, DP_REQUEST_CODE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, DP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == DP_REQUEST_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    ShowSelectedImage.setImageBitmap(FixBitmap);
                    UploadImageOnServerButton.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void UploadImageToServer() {
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            // Utility method to check if the activity is finishing or already finished
            private boolean isActivityValid(Activity activity) {
                return activity != null && !activity.isFinishing() && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed());
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (isActivityValid(EditProfile.this)) {
                    progressDialog = ProgressDialog.show(EditProfile.this, "Image is Uploading", "Please Wait", false, false);
                }
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                if (isActivityValid(EditProfile.this)) {
                    progressDialog.dismiss();

                    // Display the message from PHP in a Toast
                    Log.d("UploadImageToServer", "Response: " + string1);
                    Toast.makeText(EditProfile.this, string1, Toast.LENGTH_LONG).show();

                    // Check if the profile was updated successfully
                    try {
                        // Check if the response is an array
                        if (string1.startsWith("[")) {
                            JSONArray jsonArray = new JSONArray(string1);
                            // Handle JSONArray if needed
                        } else {
                            // It's assumed to be an object
                            JSONObject jsonResponse = new JSONObject(string1);
                            int status = jsonResponse.getInt("Status");
                            if (status == 1) {
                                Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfile.this, "Profile updation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                EditProfile.ImageProcessClass imageProcessClass = new EditProfile.ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<>();
                HashMapParams.put("user_id", String.valueOf(userId));  // Pass the user ID here
                HashMapParams.put(ImageTag, GetImageNameFromEditText);
                HashMapParams.put(ImageName, ConvertImage);
                HashMapParams.put(UserNameTag, userName.getText().toString());
                HashMapParams.put(PhoneTag, phone.getText().toString());
                HashMapParams.put(CountryTag, selectedCountry);
                HashMapParams.put(CityTag, selectedCity);

                try {
                    String FinalData = imageProcessClass.ImageHttpRequest("http://192.168.10.51/assign3smd/editprofile.php", HashMapParams);
                    Log.d("UploadImageToServer", "FinalData: " + FinalData);
                    return FinalData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error during image upload and profile update";
                }
            }
        }

        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(requestURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int RC = httpURLConnection.getResponseCode();
                if (RC == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException, UnsupportedEncodingException {
            StringBuilder stringBuilder = new StringBuilder();
            boolean check = true;
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
    }

    private static final String PROGRESS_DIALOG_VISIBLE_KEY = "progressDialogVisible";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            outState.putBoolean(PROGRESS_DIALOG_VISIBLE_KEY, true);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(PROGRESS_DIALOG_VISIBLE_KEY)) {
            showProgressDialog();
        }
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(EditProfile.this, "Image is Uploading", "Please Wait", false, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        // Close the database helper when the activity is destroyed
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

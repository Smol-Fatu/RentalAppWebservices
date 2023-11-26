package com.fatimamustafa.assignment3_20i0564_20i0445;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button GetVideoFromGalleryButton, UploadVideoOnServerButton;
    VideoView ShowSelectedVideo;

    private Uri selectedVideoUri;

    private static final int VIDEO_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // Permission not granted, request it
                String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, VIDEO_GALLERY);
            }
        }

        GetVideoFromGalleryButton = findViewById(R.id.buttonSelect);
        UploadVideoOnServerButton = findViewById(R.id.buttonUpload);
        ShowSelectedVideo = findViewById(R.id.videoView);

        GetVideoFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVideoFromGallery();
            }
        });

        UploadVideoOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedVideoUri != null) {
                    UploadVideoToServer(selectedVideoUri);
                } else {
                    Toast.makeText(MainActivity.this, "Please select a video first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == VIDEO_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your functionality
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseVideoFromGallery() {
        Intent videoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(videoIntent, VIDEO_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == VIDEO_GALLERY) {
                selectedVideoUri = data.getData();
                ShowSelectedVideo.setVideoURI(selectedVideoUri);
                UploadVideoOnServerButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void UploadVideoToServer(Uri videoUri) {
        ShowSelectedVideo.stopPlayback(); // Stop video playback for uploading

        String videoPath = getRealPathFromURI(videoUri);

        if (videoPath != null) {
            new UploadVideoAsyncTask().execute(videoPath);
            Log.d("VideoUri", videoUri.toString());
        } else {
            Toast.makeText(MainActivity.this, "Error retrieving video path", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the real path from URI
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }

        return null;
    }

    private class UploadVideoAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Uploading Video...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String videoPath = params[0];
            String videoName = "video_name"; // Replace with your desired video name

            try {
                URL url = new URL("http://192.168.10.51/assign3smd/uploadvideo.php"); // Replace with your server URL
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bytes = Base64.encode(Files.readAllBytes(Paths.get(videoPath)), Base64.DEFAULT);
                String videoData = new String(bytes);

                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("video_data", videoData);
                paramsMap.put("video_tag", videoName);

                bufferedWriter.write(getPostDataString(paramsMap));

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    bufferedReader.close();
                    return response.toString();
                } else {
                    return "Error uploading video. HTTP response code: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Error", e.getMessage());
                return "Error uploading video: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }

        private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }
}

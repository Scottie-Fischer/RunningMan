package com.example.runandtrack;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Profile extends AppCompatActivity {
    public static final int Image_Gallery_Request = 20;
    private ImageView Profile_Picture;
    SharedPreferences sh;
    EditText editWeight, editName, editAge, editSex;

    //This method will be invoked when the user clicks the edit button
    public void onImageGalleryClicked(View v){
        //invoke the image gallery using an implicit intent
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        //decides where to store pictures
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        //gets URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        //sets the data and type of media to look for
        photoPickerIntent.setDataAndType(data,"image/*");
        startActivityForResult(photoPickerIntent, Image_Gallery_Request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Profile_Picture = (ImageView) findViewById(R.id.Profile_Picture);

        sh = getSharedPreferences("runProfile", MODE_PRIVATE);
        editWeight = findViewById(R.id.profileWeight);
        editName = findViewById(R.id.profileName);
        editAge = findViewById(R.id.profileAge);
        editSex = findViewById(R.id.profileSex);

        Float weight = sh.getFloat("profileWeight", -1);
        int age = sh.getInt("profileAge", -1);
        String name = sh.getString("profileName", "");
        String sex = sh.getString("profileSex", "");

        if (weight != -1) {
            editWeight.setText(Float.toString(weight));
        }
        if (age != -1) {
            editAge.setText(Integer.toString(age));
        }
        editName.setText(name);
        editSex.setText(sex);
        Button ProfileHomeButton = (Button)findViewById(R.id.ProfileHomeButton);
        ProfileHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //On Activity for Profile Picture Button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //everything processed successfully
            if (requestCode == Image_Gallery_Request) {
                //image gallery correctly responds
                Uri imageUri = data.getData();  //address of image on SD Card
                InputStream inputStream;        //declaring stream to read data from SD card
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap imagebtmp = BitmapFactory.decodeStream(inputStream);       //Puts stream data into bitmap format
                    Profile_Picture.setImageBitmap(imagebtmp);                        //Loads Image to ImageView

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //error message if image is unavailable
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = sh.edit();
        if (!editWeight.getText().toString().isEmpty())
            edit.putFloat("profileWeight", Float.parseFloat(editWeight.getText().toString()));
        if (!editAge.getText().toString().isEmpty())
            edit.putInt("profileAge", Integer.parseInt(editAge.getText().toString()));
        edit.putString("profileName", editName.getText().toString());
        edit.putString("profileSex", editSex.getText().toString());

        Bitmap bm = ((BitmapDrawable)Profile_Picture.getDrawable()).getBitmap();
        //Profile_Picture.setImageBitmap(bm);
        edit.apply();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bitmap bm = ((BitmapDrawable)Profile_Picture.getDrawable()).getBitmap();
        outState.putParcelable("Profile_Bitmap",bm);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bitmap bm = savedInstanceState.getParcelable("Profile_Bitmap");
        Profile_Picture.setImageBitmap(bm);
    }
}

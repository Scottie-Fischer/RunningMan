package com.example.runandtrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Story 2 - Initializing Button

        mImageView = findViewById(R.id.Profile_Picture);
        mChooseBtn = findViewById(R.id.Edit_Profile_Picture_Button);

        //handle button click
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted so we request
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else { //permission granted
                        pickImageFromGallery();
                    }
                } else {
                    //permission already granted
                }
            }
        });

        private void pickImageFromGallery(){
            //intent to pick image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivity(intent,IMAGE_PICK_CODE);
        }
        @Override
        protected void onActivityResult(int requestCode,int resultCode,Intent data){
            if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
                //set image to image view
                mImageView.setImageURI(data.getData());
            }
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
            //super.onRequestPermissionsResult(requestCode,permissions,grantResults);
            switch(requestCode){
                case PERMISSION_CODE:
                    if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        //permission was granted
                        pickImageFromGallery();
                    }
                    else{//permission denied
                        Toast.makeText(this, "Permission denied",Toast.LENGTH_SHORT).show();
                    }
            }
        }
        //End of Story 2
        Button ProfileHomeButton = (Button)findViewById(R.id.ProfileHomeButton);
        ProfileHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

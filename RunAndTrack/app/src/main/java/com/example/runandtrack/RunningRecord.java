package com.example.runandtrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RunningRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_record);
    }

    public void saveRecord(View view) {
        Intent firstIntent= new Intent(this, History.class);
        startActivityForResult(firstIntent, 1);
    }

    // Redirect to the History page once cancel button is clicked on
    // Will not make any changes to the database
    public void cancelRecord(View view){
        Intent firstIntent= new Intent(this,History.class);
        startActivityForResult(firstIntent,1);
    }
}

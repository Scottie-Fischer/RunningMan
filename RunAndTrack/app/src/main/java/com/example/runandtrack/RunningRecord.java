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
}

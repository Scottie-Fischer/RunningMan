package com.example.runandtrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RunningMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode);

    }

    public void onClickCancel(View view){
        Intent firstIntent= new Intent(this, MainActivity.class);
        startActivityForResult(firstIntent, 1);
    }

    public void onClickEnd(View view){
        Intent secondIntent= new Intent(this,RunningRecord.class);
        startActivityForResult(secondIntent,1);
    }

}

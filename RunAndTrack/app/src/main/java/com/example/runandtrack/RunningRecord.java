package com.example.runandtrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RunningRecord extends AppCompatActivity {
	public static String RUN_DISTANCE = "run_distance";
    public static String RUN_TIME = "run_time";
    public static String RUN_AVGSPEED = "run_avgspeed";
    EditText editDistance, editTime, editAvgSpeed;
    float distance, avgspeed;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_record);
        View recordDelete = findViewById(R.id.deleteRecord);
        /*if(recordDelete.getVisibility() == View.GONE){
            recordDelete.setVisibility(View.GONE);
        }
        else{
            recordDelete.setVisibility(View.VISIBLE);
        }*/
        editDistance = findViewById(R.id.recordDistance);
        editTime = findViewById(R.id.recordTime);
        editAvgSpeed = findViewById(R.id.recordSpeed);

        Intent intent = getIntent();
        distance = (float)intent.getDoubleExtra(RUN_DISTANCE, 0.001f);
        time = intent.getIntExtra(RUN_TIME, 600);
        avgspeed = (float)intent.getDoubleExtra(RUN_AVGSPEED, 0.001f);
        editDistance.setText(Float.toString(distance));
        editTime.setText(Integer.toString(time));
        editAvgSpeed.setText(Float.toString(avgspeed));
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

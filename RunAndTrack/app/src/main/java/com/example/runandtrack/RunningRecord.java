package com.example.runandtrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static java.lang.Float.parseFloat;

public class RunningRecord extends AppCompatActivity {
	public static String RUN_DISTANCE = "run_distance";
    public static String RUN_TIME = "run_time";
    public static String RUN_CALORIES = "run_calories";
    public static final String SHOULD_SHOW = "should";

    private Button saveBtn;
    private Button recordDelete;
    private EditText recordTime;
    SharedPreferences sh;
    RunDatabase db;
    EditText editDistance, editTime, editCalories;
    float distance, weight;
    int time, calories;
    boolean showButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_record);
        saveBtn = findViewById(R.id.saveRecord);
        db = RunDatabase.getInstance(this);

        recordDelete = findViewById(R.id.deleteRecord);

        editDistance = findViewById(R.id.recordDistance);
        editTime = findViewById(R.id.recordTime);
        editCalories = findViewById(R.id.recordCalories);

        //Makes the Text Not Editable By the User
        editDistance.setKeyListener(null);
        editTime.setKeyListener(null);
        editCalories.setKeyListener(null);


        Intent intent = getIntent();
        distance = (float)intent.getDoubleExtra(RUN_DISTANCE, 0.001f);
        time = intent.getIntExtra(RUN_TIME, 600);
        calories = intent.getIntExtra(RUN_CALORIES, -1);
        editDistance.setText(Float.toString(distance));
        editTime.setText(Integer.toString(time));

        showButton = intent.getBooleanExtra(SHOULD_SHOW, true);
        if(showButton == false) {
            saveBtn.setVisibility(View.INVISIBLE);
            recordDelete.setVisibility(View.VISIBLE);
        } else {
            saveBtn.setVisibility(View.VISIBLE);
            recordDelete.setVisibility(View.GONE);
        }

        //Calculate the calories being burnt per run
        if (calories == -1) {
            sh = getSharedPreferences("runProfile", MODE_PRIVATE);
            weight = sh.getFloat("profileWeight", 60);
            float hours = time / 3600.0f;
            float minutes = time / 60.0f;
            float meters = distance * 1000.0f;
            float speed = minutes / (meters / 400.0f);
            Log.d("RunningRecord", hours + ", " + minutes + ", " + meters + ", " + speed);
            calories = Math.round(weight * hours * 30 / speed);
        }
        editCalories.setText(Integer.toString(calories));
    }

    public void saveRecord(View view) {
        db.insert(Integer.parseInt(editCalories.getText().toString()),
                parseFloat(editDistance.getText().toString()),
                Integer.parseInt(editTime.getText().toString()));
        finish();
        Intent firstIntent= new Intent(this, History.class);
        startActivityForResult(firstIntent, 1);
    }

    // Redirect to the History page once cancel button is clicked on
    // Will not make any changes to the database
    public void cancelRecord(View view){
        Intent firstIntent= new Intent(this,History.class);
        startActivityForResult(firstIntent,1);
    }

    //Redirects to the History page
    //Pulls data from the date entry in order to delete the entry from SQL database
    public void deleteRecord(View view){
        db = RunDatabase.getInstance(this);
        recordTime = findViewById(R.id.recordTime);
        String DATE = recordTime.getText().toString();
        System.out.println("DATE: " + DATE);
        db.remove(DATE);
        Intent firstIntent = new Intent(this,History.class);
        startActivityForResult(firstIntent,1);
    }
}

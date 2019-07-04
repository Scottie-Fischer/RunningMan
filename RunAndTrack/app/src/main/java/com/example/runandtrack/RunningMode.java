package com.example.runandtrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RunningMode extends AppCompatActivity {
	//Timer
    private int seconds = 0;
    private boolean running;
    //Button
    private Button StartAndPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode);
        //Call the runTimer()
        runTimer();
        //Set the Start and Pause Button
        StartAndPause = findViewById(R.id.Start_Pause_button);

        StartAndPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(running){
                    onClickPause(v);
                }else{
                    onClickStart(v);
                }
            }
        });
    }

    //Start the timer when the Start button is clicked 
    public void onClickStart(View view){
        running = true;
        StartAndPause.setText("Pause");
    }
    
    //Pause the timer when the Pause button is clicked 
    public void onClickPause(View view){
        running = false;
        StartAndPause.setText("Start");
    }

    //Cancel the timer when the Cancel button is clicked 
    public void onClickCancel(View view){
        running = false;
        seconds = 0;
        Intent firstIntent= new Intent(this, MainActivity.class);
        startActivityForResult(firstIntent, 1);
    }

    //End the timer when the End button is clicked 
    public void onClickEnd(View view){
        running = false;
        Intent secondIntent= new Intent(this,RunningRecord.class);
        seconds = 0;
        startActivityForResult(secondIntent,1);
    }

    //Sets the number of seconds
    private void runTimer() {
        final TextView timeView = (TextView)findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000); //Second = 1000* million seconds
            }
        });
    }
}
package com.example.runandtrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import static java.lang.Float.parseFloat;

public class RunningRecord extends AppCompatActivity implements OnMapReadyCallback {
	public static String RUN_DISTANCE = "run_distance";
    public static String RUN_TIME = "run_time";
    public static String RUN_CALORIES = "run_calories";
    public static String RUN_DATE = "run_date";
    public static String RUN_END = "run_end";
    public static final String SHOULD_SHOW = "should";

    private Button saveBtn;
    private Button recordDelete;
    private EditText recordDate,editDate;
    SharedPreferences sh;
    RunDatabase db;
    EditText editDistance, editTime, editCalories;
    float distance, weight;
    int time, calories;
    boolean showButton;

    //Map
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Bundle endPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_record);
        saveBtn = findViewById(R.id.saveRecord);
        db = RunDatabase.getInstance(this);

        //Set up map
        initMap();

        recordDelete = findViewById(R.id.deleteRecord);

        editDistance = findViewById(R.id.recordDistance);
        editTime = findViewById(R.id.recordTime);
        editCalories = findViewById(R.id.recordCalories);
        editDate = findViewById(R.id.recordDate);

        //Makes the Text Not Editable By the User
        editDistance.setKeyListener(null);
        editTime.setKeyListener(null);
        editCalories.setKeyListener(null);
        editDate.setKeyListener(null);

        //Get the running related data
        Intent intent = getIntent();
        distance = intent.getFloatExtra(RUN_DISTANCE, 0.001f);
        time = intent.getIntExtra(RUN_TIME, 600);
        String date = intent.getStringExtra(RUN_DATE);
        calories = intent.getIntExtra(RUN_CALORIES, -1);
        endPosition = intent.getBundleExtra(RUN_END);

        editDistance.setText(Float.toString(distance));
        editTime.setText(Integer.toString(time));
        editDate.setText(date);

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

    //Obtain the SupportMapFragment and get notified when the map is ready to be used.
    public void initMap() {
        if (mMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
            mapFragment.getMapAsync(this);
        }
    }

    //Initial display of map when it is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        LatLng endCoord = null;
        Boolean emptyMap = true;

        //And running end position to the map
        if(endPosition != null){
            endCoord = new LatLng(endPosition.getDouble("endLat"), endPosition.getDouble("endLng"));
            mMap.addMarker(new MarkerOptions().position(endCoord).title("B")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            bounds.include(endCoord);
            emptyMap = false;
        }else{
            //Don't show the map if there are no end coordinates
            //This mean the user ends the run without pressing START
            mapFragment.getView().setVisibility(View.INVISIBLE);
        }
        //auto-zoom and auto-center map where markers are located
        if(!emptyMap) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    //Redirects to the History page
    //Saves the running data to the database
    public void saveRecord(View view) {
        System.out.println("Saving date: " + editDate);
        db.insert(Integer.parseInt(editCalories.getText().toString()),
                parseFloat(editDistance.getText().toString()),
                Integer.parseInt(editTime.getText().toString()),(editDate.getText().toString()));
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
        recordDate = findViewById(R.id.recordDate);

        String DATE = recordDate.getText().toString();
        db.remove(DATE);

        //redirects to History Page
        Intent firstIntent = new Intent(this,History.class);
        startActivityForResult(firstIntent,1);
    }
}

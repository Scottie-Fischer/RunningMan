package com.example.runandtrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class RunningMode extends AppCompatActivity implements OnMapReadyCallback {
    //Timer
    private int seconds = 0;
    private boolean running;
    //Button
    private Button StartAndPause;
    //GPS
    private LocationManager lm;
    private final int REQUEST_LOCATION = 42;
    private boolean granted = false;
    private LinkedList<Location> locations;
    //Distance and speed
    private String decimalPoint = "#.######";
    private String ms = " m/s";//meters per second
    private String km = " km"; //kilometers
    private DecimalFormat decimal = new DecimalFormat(decimalPoint);
    private TextView totalDistanceNew, averageSpeedNew;
    private float totalDistanceValue, averageSpeedValue;
    private String totalDistanceString, averageSpeedString;
    //Map
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Marker endMarker;
    private LatLng endLatLgn = null;
    private Bundle endPosition =  new Bundle();

    View recordDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Loads in the Record Delete Button from Running Record Layout
        setContentView(R.layout.activity_running_record);
        recordDelete = findViewById(R.id.deleteRecord);

        setContentView(R.layout.activity_running_mode);
        //Call the runTimer()
        runTimer();
        ///Call getLayoutElements()
        getLayoutElements();
        //Set up the GPS
        if (checkPermission())
            lm = getLocationManager();
        //Set the Start and Pause Button
        StartAndPause = findViewById(R.id.Start_Pause_button);
        //Set up map
        initMap();

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

    //Obtain the SupportMapFragment and get notified when the map is ready to be used.
    public void initMap() {
        if (mMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routeMap);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        //Get current location
        if ((granted || checkPermission()) && !running) {
            mMap.setMyLocationEnabled(true);
        }
    }

    //Start the timer when the Start button is clicked 
    public void onClickStart(View view){
        mMap.setMyLocationEnabled(false);
        running = true;
        StartAndPause.setText("Pause");
        if (granted || checkPermission()) {
            startRecording();
        }
    }
    //Pause the timer when the Pause button is clicked 
    public void onClickPause(View view){
        running = false;
        StartAndPause.setText("Start");
        stopRecording();
    }
    //Cancel the timer when the Cancel button is clicked 
    public void onClickCancel(View view){
        running = false;
        seconds = 0;
        totalDistanceValue = 0;
        averageSpeedValue = 0;
        Intent firstIntent= new Intent(this, MainActivity.class);
        startActivityForResult(firstIntent, 1);
    }
    //End the timer when the End button is clicked 
    public void onClickEnd(View view){
        running = false;

        recordDelete.setVisibility(view.VISIBLE);

        //Get the date when the run happens
        Date date = new Date();
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss zzz");
        String newDate = newDateFormat.format(date);

        Intent secondIntent= new Intent(this,RunningRecord.class);
        secondIntent.putExtra(RunningRecord.RUN_TIME, seconds);
        secondIntent.putExtra(RunningRecord.RUN_DISTANCE, totalDistanceValue);
        secondIntent.putExtra(RunningRecord.RUN_DATE, newDate);

        //Pass current location coordinates to Running Record page
        if(endLatLgn != null) {
            endPosition.putDouble("endLat", endLatLgn.latitude);
            endPosition.putDouble("endLng", endLatLgn.longitude);
            secondIntent.putExtra(RunningRecord.RUN_END, endPosition);
        }

        seconds = 0;
        totalDistanceValue = 0;
        averageSpeedValue = 0;
        startActivityForResult(secondIntent,1);
    }
    ///Get the Strings and TextView
    private void getLayoutElements() {
        totalDistanceNew = (TextView) findViewById(R.id.total_distance_id);
        averageSpeedNew = (TextView) findViewById(R.id.average_speed_id);
        totalDistanceString = getString(R.string.total_distance_s);
        averageSpeedString = getString(R.string.average_speed_s);
    }
    // Get GPS permission
    private boolean checkPermission() {
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (check != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            granted = true;
        }
        return (granted);
    }
    //Get request results and Setup GPS if permission request was accepted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            granted = true;
            lm = getLocationManager();
        }
    }

    // Get Location Manager
    private LocationManager getLocationManager() {
        return ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
    }

    //Start the GPS
    private void startGPS() {

        // Request update every 400ms with 1 meter minimum distance difference
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, listener);
        locations = new LinkedList<>();
        totalDistanceNew.setText(totalDistanceString);
        averageSpeedNew.setText(averageSpeedString);
    }

    //Stop the GPS
    private void stopGPS() {
        lm.removeUpdates(listener);
    }

    // Start getting position
    private void startRecording() {
        startGPS();
    }

    // Stop getting position
    private void stopRecording() {
        stopGPS();
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


    // GPS listener
    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (!locations.isEmpty()) {
                //Update Distance
                totalDistanceValue += (location.distanceTo(locations.peekLast()) / 1000); // unit: km
                totalDistanceNew.setText(totalDistanceString + decimal.format(totalDistanceValue) + km);
            }
            //Update average speed
            averageSpeedValue = (totalDistanceValue*1000)/seconds; // unit: m/s
            averageSpeedNew.setText(averageSpeedString + decimal.format(averageSpeedValue) + ms);
            // Update new position
            locations.add(location);
            //Add an azure marker to current position in map
            if (endMarker != null) {
                endMarker.remove();
            };
            endLatLgn = new LatLng(location.getLatitude(), location.getLongitude());
            endMarker = mMap.addMarker(new MarkerOptions().position(endLatLgn).title("Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLgn, 16));
        }
        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
}

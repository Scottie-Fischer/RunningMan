package com.example.runandtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class History extends AppCompatActivity implements RecordAdapter.OnRecordListener {
    RunDatabase db;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RecordHelper> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button HistoryHomeButton = (Button)findViewById(R.id.HistoryHomeButton);
        HistoryHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        db = RunDatabase.getInstance(this);

        recyclerView = findViewById(R.id.historyRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        list = db.view();
        adapter = new RecordAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRecordClick(int position) {
        Intent intent= new Intent(this,RunningRecord.class);
        RecordHelper record = list.get(position);
        float distance = record.getRecordDistance();
        int seconds = record.getRecordTime();
        int calories = record.getRecordCalories();
        String date = record.getRecordDate();
        intent.putExtra(RunningRecord.RUN_DISTANCE, distance);
        intent.putExtra(RunningRecord.RUN_TIME, seconds);
        intent.putExtra(RunningRecord.RUN_CALORIES, calories);
        intent.putExtra(RunningRecord.RUN_DATE,date);
        intent.putExtra(RunningRecord.SHOULD_SHOW, false);
        startActivity(intent);
    }
}


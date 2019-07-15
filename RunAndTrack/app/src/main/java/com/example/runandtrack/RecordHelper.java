package com.example.runandtrack;

public class RecordHelper {
    private int recordId;
    private String recordDate;
    private int recordCalories;
    private float recordDistance;
    private int recordTime;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int id) {
        this.recordId = id;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String date) {
        this.recordDate = date;
    }

    public int getRecordCalories() {
        return recordCalories;
    }

    public void setRecordCalories(int calories) {
        this.recordCalories = calories;
    }

    public float getRecordDistance() {
        return recordDistance;
    }

    public void setRecordDistance(float distance) {
        this.recordDistance = distance;
    }

    public int getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(int time) {
        this.recordTime = time;
    }
}

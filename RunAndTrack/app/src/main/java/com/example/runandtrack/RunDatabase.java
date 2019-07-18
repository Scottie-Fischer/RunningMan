package com.example.runandtrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class RunDatabase extends SQLiteOpenHelper {
    private static RunDatabase db;
    static String DB_NAME = "RUN_DATABASE";
    static String TABLE_NAME = "RUN_TABLE";
    static String COL_ID = "_id";
    static String COL_DATE = "date";
    static String COL_CALORIES = "calories";
    static String COL_DISTANCE = "distance";    // in km
    static String COL_TIME = "time";            // in seconds
    static int VERSION = 1;

    public static RunDatabase getInstance(Context context) {
        Log.d("Database", "Getting instance");
        if (db == null) {
            db = new RunDatabase(context);
        }
        return db;
    }

    private RunDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_DATE + " INTEGER DEFAULT (date('now','localtime')), " +
                COL_CALORIES + " INTEGER, " +
                COL_DISTANCE + " REAL, " +
                COL_TIME + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(int calories, float distance, int time) {
        Log.d("Database", "Inserting");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CALORIES, calories);
        cv.put(COL_DISTANCE, distance);
        cv.put(COL_TIME, time);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }
    public void remove(String time){
        SQLiteDatabase db = this.getWritableDatabase();
        //String ID = getID(time);
        try {
            db.delete(TABLE_NAME, COL_TIME + " = ?", new String[]{time});
        }
        catch(Exception e){
            e.printStackTrace();
        }
        db.close();

    }
    /*
    public Cursor getID(int time){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT" + COL_ID + " FROM " + TABLE_NAME + " WHERE " + COL_TIME + " = " + time;
        Cursor data = db.rawQuery(query,null);
        return data;
    }*/

    public ArrayList<RecordHelper> view() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<RecordHelper> list = new ArrayList<RecordHelper>();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_DATE + " DESC";
        Log.d("RunDatabase", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("RunDatabase", "Extracting");
                RecordHelper record = new RecordHelper();
                record.setRecordId(cursor.getInt(0));
                record.setRecordDate(cursor.getString(1));
                record.setRecordCalories(cursor.getInt(2));
                record.setRecordDistance(cursor.getFloat(3));
                record.setRecordTime(cursor.getInt(4));
                list.add(record);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }
}

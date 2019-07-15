package com.example.runandtrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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



}

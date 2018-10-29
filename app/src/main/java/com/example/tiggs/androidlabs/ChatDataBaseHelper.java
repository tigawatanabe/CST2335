package com.example.tiggs.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDataBaseHelper extends SQLiteOpenHelper {

    //database name
    protected static final String DATABASE_NAME = "Lab5";
    //database version
    protected static final int VERSION_NUM = 2;
    //table name
    public static final String TABLE_NAME = "ThisTable";
    //activity name
    protected static final String ACTIVITY_NAME = "This_Activity";
    //some columns
    protected static final String KEY_ID = "ID";
    protected static final String KEY_MESSAGE = "MESSAGE";

    public ChatDataBaseHelper(Context ctx){
        super(ctx, DATABASE_NAME,null,VERSION_NUM);
    }
    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT);");
        //add message
        Log.i(ACTIVITY_NAME, "Calling onCreate()");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        //drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //add message
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + "newVersion=" + newVer);
        //Create tables again
        onCreate(db);
  }
}
